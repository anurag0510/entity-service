package org.logistics.entityService.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.logistics.entityService.constants.EntityTypes;
import org.logistics.entityService.controller.PlaceController;
import org.logistics.entityService.controller.UserController;
import org.logistics.entityService.data.entities.CompanyEntity;
import org.logistics.entityService.data.repositories.CompanyRepository;
import org.logistics.entityService.exceptions.EntityServiceException;
import org.logistics.entityService.kafka.Producer;
import org.logistics.entityService.model.request.UpdatePlaceRequestModel;
import org.logistics.entityService.model.response.CreatePlaceResponseModel;
import org.logistics.entityService.service.CompanyService;
import org.logistics.entityService.shared.CompanyDto;
import org.logistics.entityService.shared.Utils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;
    private Producer event;
    private Utils utils;
    PlaceController placeController;
    UserController userController;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, Producer event, Utils utils, PlaceController placeController, UserController userController) {
        this.utils = utils;
        this.companyRepository = companyRepository;
        this.placeController = placeController;
        this.userController = userController;
        this.event = event;
    }

    @Override
    public CompanyDto createCompany(CompanyDto companyDto, String requesterId) {
        log.info("company : {} and requester : {}", companyDto, requesterId);
        ResponseEntity<CreatePlaceResponseModel> createdPlaceDetails = null;
        ResponseEntity<CreatePlaceResponseModel> createdHeadOfficeDetails = null;
        companyDto.setCid(EntityTypes.COM.name() + "-" + utils.getUniqueId());
        companyDto.setIsActive(true);
        companyDto.setIsDeleted(false);
        companyDto.setCreatedBy(requesterId);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
        CompanyEntity companyEntity = modelMapper.map(companyDto, CompanyEntity.class);
        companyRepository.saveAndFlush(companyEntity);
        modelMapper.map(companyEntity, companyDto);
        event.sendCompanyEvent("create_company", companyDto);
        if (companyDto.getPlace() != null) {
            companyDto.getPlace().setParentId(companyDto.getCid());
            createdPlaceDetails = placeController.createPlace(companyDto.getPlace(), requesterId);
            companyDto.setPlaceId(createdPlaceDetails.getBody().getPid());
        }
        if (companyDto.getHeadOffice() != null) {
            companyDto.getHeadOffice().setParentId(companyDto.getCid());
            createdHeadOfficeDetails = placeController.createPlace(companyDto.getHeadOffice(), requesterId);
            companyDto.setHeadOfficeId(createdHeadOfficeDetails.getBody().getPid());
        }
        this.updateCompany(companyDto.getCid(), companyDto, requesterId, false);
        return companyDto;
    }

    @Override
    public List<CompanyDto> getAllCompanies(boolean allCompanies) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
        List<CompanyEntity> post = allCompanies == false ? (List<CompanyEntity>) companyRepository.findAllCompaniesWhereActiveStateIs(true) : (List<CompanyEntity>) companyRepository.findAll();
        Type listType = new TypeToken<List<CompanyDto>>() {
        }.getType();
        return modelMapper.map(post, listType);
    }

    @Override
    public List<CompanyDto> getAllCompaniesBasedOnCid(String cid, boolean allCompanies) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
        List<CompanyEntity> post = !allCompanies ? (List<CompanyEntity>) companyRepository.findAllCompaniesBasedOnCidAndAreActive(cid) : (List<CompanyEntity>) companyRepository.findAllByCid(cid);
        Type listType = new TypeToken<List<CompanyDto>>() {
        }.getType();
        return modelMapper.map(post, listType);
    }

    @Override
    public CompanyDto updateCompany(String companyId, CompanyDto companyDto, String requesterId, boolean considerPlacesModification) {
        List<CompanyEntity> companyEntityList = (List<CompanyEntity>) companyRepository.findAllCompaniesBasedOnCidAndAreActive(companyId);
        if (companyEntityList.size() == 0)
            throw new EntityServiceException("No companies found!!!");
        CompanyEntity companyEntity = companyEntityList.get(0);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
        companyDto.setUpdatedAt(new Date());
        companyDto.setUpdatedBy(requesterId);
        if (companyDto.getTypes() != null) companyEntity.setTypes(companyDto.getTypes());
        modelMapper.map(companyDto, companyEntity);
        modelMapper.map(companyEntity, companyDto);
        if (considerPlacesModification) {
            modifyPlaceDetails(companyDto, companyEntity, requesterId);
            modifyHeadOfficeDetails(companyDto, companyEntity, requesterId);
        }
        companyRepository.saveAndFlush(companyEntity);
        event.sendCompanyEvent("update_company", companyDto);
        return companyDto;
    }

    @Override
    public CompanyDto updateCompanyStatus(String companyId, CompanyDto companyDto, String requesterId) {
        List<CompanyEntity> companyEntityList = (List<CompanyEntity>) companyRepository.findAllByCid(companyId);
        if (companyEntityList.size() == 0)
            throw new EntityServiceException("No companies found!!!");
        CompanyEntity companyEntity = companyEntityList.get(0);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
        companyDto.setUpdatedAt(new Date());
        companyDto.setUpdatedBy(requesterId);
        modelMapper.map(companyDto, companyEntity);
        modelMapper.map(companyEntity, companyDto);
        companyRepository.saveAndFlush(companyEntity);
        event.sendCompanyEvent("update_company", companyDto);
        return companyDto;
    }

    @Override
    public CompanyDto deleteCompany(CompanyDto companyDto, String requesterId) {
        List<CompanyEntity> companyEntityList = (List<CompanyEntity>) companyRepository.findAllByCid(companyDto.getCid());
        if (companyEntityList.size() == 0)
            throw new EntityServiceException("No companies found!!!");
        CompanyEntity companyEntity = companyEntityList.get(0);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
        companyDto.setUpdatedAt(new Date());
        if (userController.getUser(false, "uid", requesterId).getStatusCode().is2xxSuccessful())
            companyDto.setUpdatedBy(requesterId);
        else
            throw new EntityServiceException("Unauthorized user trying to modify entity details");
        companyDto.setIsDeleted(true);
        companyDto.setDeletedAt(new Date());
        modelMapper.map(companyDto, companyEntity);
        modelMapper.map(companyEntity, companyDto);
        companyRepository.saveAndFlush(companyEntity);
        event.sendCompanyEvent("delete_company", companyDto);
        return companyDto;
    }

    private void modifyPlaceDetails(CompanyDto companyDto, CompanyEntity companyEntity, String requesterId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
        ResponseEntity<CreatePlaceResponseModel> createdPlaceDetails = null;
        if (companyDto.getPlace() != null && companyDto.getPlaceId() != null) {
            companyDto.getPlace().setParentId(companyDto.getCid());
            UpdatePlaceRequestModel updatePlaceRequestModel = modelMapper.map(companyDto.getPlace(), UpdatePlaceRequestModel.class);
            placeController.updatePlace(updatePlaceRequestModel, requesterId, "pid", companyDto.getPlaceId());
        } else if (companyDto.getPlace() != null && companyDto.getPlaceId() == null) {
            companyDto.getPlace().setParentId(companyDto.getCid());
            createdPlaceDetails = placeController.createPlace(companyDto.getPlace(), requesterId);
            companyDto.setPlaceId(createdPlaceDetails.getBody().getPid());
            companyEntity.setPlaceId(createdPlaceDetails.getBody().getPid());
        } else if (companyDto.getPlace() == null && companyDto.getPlaceId() != null) {
            companyDto.setPlaceId(null);
            companyEntity.setPlaceId(null);
        }
    }

    private void modifyHeadOfficeDetails(CompanyDto companyDto, CompanyEntity companyEntity, String requesterId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
        ResponseEntity<CreatePlaceResponseModel> createdHeadOfficeDetails = null;
        if (companyDto.getHeadOffice() != null && companyDto.getHeadOfficeId() != null) {
            companyDto.getHeadOffice().setParentId(companyDto.getCid());
            UpdatePlaceRequestModel updatePlaceRequestModel = modelMapper.map(companyDto.getHeadOffice(), UpdatePlaceRequestModel.class);
            placeController.updatePlace(updatePlaceRequestModel, requesterId, "pid", companyDto.getHeadOfficeId());
        } else if (companyDto.getHeadOffice() != null && companyDto.getHeadOfficeId() == null) {
            companyDto.getHeadOffice().setParentId(companyDto.getCid());
            createdHeadOfficeDetails = placeController.createPlace(companyDto.getHeadOffice(), requesterId);
            companyDto.setHeadOfficeId(createdHeadOfficeDetails.getBody().getPid());
            companyEntity.setHeadOfficeId(createdHeadOfficeDetails.getBody().getPid());
        } else if (companyDto.getHeadOffice() == null && companyDto.getHeadOfficeId() != null) {
            companyDto.setHeadOfficeId(null);
            companyEntity.setHeadOfficeId(null);
        }
    }
}
