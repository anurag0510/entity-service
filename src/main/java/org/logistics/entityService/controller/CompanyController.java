package org.logistics.entityService.controller;

import lombok.extern.slf4j.Slf4j;
import org.logistics.entityService.exceptions.EntityServiceException;
import org.logistics.entityService.model.request.CreateCompanyRequestModel;
import org.logistics.entityService.model.request.UpdateEntityActiveStatusRequestModel;
import org.logistics.entityService.model.request.UpdateCompanyRequestModel;
import org.logistics.entityService.model.request.UpdatePlaceRequestModel;
import org.logistics.entityService.model.response.CreateCompanyResponseModel;
import org.logistics.entityService.model.response.CreatePlaceResponseModel;
import org.logistics.entityService.model.response.GetAllCompanyResponseModel;
import org.logistics.entityService.service.CompanyService;
import org.logistics.entityService.shared.CompanyDto;
import org.logistics.entityService.shared.PlaceDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.lang.reflect.Type;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/company")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @Autowired
    PlaceController placeController;

    @PostMapping
    public ResponseEntity<CreateCompanyResponseModel> createCompany(
            @Valid @RequestBody CreateCompanyRequestModel companyDetails,
            @RequestHeader(name = "x-requester-id", required = true) @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") String requesterId
    ) {
        log.info("Company details to be created within system : {}", companyDetails);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        CompanyDto companyDto = modelMapper.map(companyDetails, CompanyDto.class);
        CreateCompanyResponseModel createCompanyResponseModel = modelMapper.map(companyService.createCompany(companyDto, requesterId), CreateCompanyResponseModel.class);
        log.info("Returning Details : {}", createCompanyResponseModel);
        return new ResponseEntity<>(createCompanyResponseModel, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{filter}/{cid}")
    public ResponseEntity<GetAllCompanyResponseModel> getCompanyBasedOnCid(
            @RequestParam(value = "all", defaultValue = "false", required = false) boolean all,
            @PathVariable(value = "filter", required = false) String filter,
            @PathVariable(value = "cid", required = false) String cid
    ) {
        log.info("Request to fetch companies details received.");
        GetAllCompanyResponseModel returnValue = new GetAllCompanyResponseModel();
        returnValue.setSuccess(true);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Type listType = new TypeToken<List<CreateCompanyResponseModel>>() {
        }.getType();
        if(!filter.matches("(?i)cid"))
            throw new EntityServiceException("Only allowed to filter via : cid");
        else {
            List<CreateCompanyResponseModel> companiesList = modelMapper.map(companyService.getAllCompaniesBasedOnCid(cid, all), listType);
            if (companiesList.size() == 0)
                throw new EntityServiceException("No companies found!!!");
            returnValue.setData(companiesList);
        }
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<GetAllCompanyResponseModel> getCompany(
            @RequestParam(value = "all", defaultValue = "false", required = false) boolean all
    ) {
        log.info("Request to fetch company details received.");
        GetAllCompanyResponseModel returnValue = new GetAllCompanyResponseModel();
        returnValue.setSuccess(true);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Type listType = new TypeToken<List<CreateCompanyResponseModel>>() {
        }.getType();
        List<CreateCompanyResponseModel> companiesList = modelMapper.map(companyService.getAllCompanies(all), listType);
        if (companiesList.size() == 0)
            throw new EntityServiceException("No companies found!!!");
        returnValue.setData(companiesList);
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @PutMapping(value = "/{filter}/{cid}")
    public ResponseEntity<CreateCompanyResponseModel> updateCompany(
            @Valid @RequestBody UpdateCompanyRequestModel companyDetails,
            @RequestHeader(name = "x-requester-id", required = true) @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") String requesterId,
            @PathVariable(value = "filter", required = true) String filter,
            @PathVariable(value = "cid", required = true) String cid
    ) {
        log.info("Request to update company details received with details : {}", companyDetails);
        if(!filter.matches("(?i)cid"))
            throw new EntityServiceException("Only allowed to filter via : cid");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        CompanyDto companyDto = modelMapper.map(companyDetails, CompanyDto.class);
        CreateCompanyResponseModel updatedCompanyResponseModel = modelMapper.map(companyService.updateCompany(cid, companyDto, requesterId), CreateCompanyResponseModel.class);
        return new ResponseEntity<>(updatedCompanyResponseModel, HttpStatus.OK);
    }

    @PatchMapping(value = "/{filter}/{cid}")
    public ResponseEntity<CreateCompanyResponseModel> changeCompanyStatus(
            @Valid @RequestBody UpdateEntityActiveStatusRequestModel statusDetails,
            @RequestHeader(name = "x-requester-id", required = true) @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") String requesterId,
            @PathVariable(value = "filter", required = true) String filter,
            @PathVariable(value = "cid", required = true) String cid
    ) {
        log.info("Request to change company status received with details : {}", statusDetails);
        if(!filter.matches("(?i)cid"))
            throw new EntityServiceException("Only allowed to filter via : cid");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        CompanyDto companyDto = modelMapper.map(statusDetails, CompanyDto.class);
        CreateCompanyResponseModel updatedCompanyResponseModel = modelMapper.map(companyService.updateCompanyStatus(cid, companyDto, requesterId), CreateCompanyResponseModel.class);
        return new ResponseEntity<>(updatedCompanyResponseModel, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{filter}/{cid}")
    public ResponseEntity<CreateCompanyResponseModel> deleteCompany(
            @RequestHeader(name = "x-requester-id", required = true) @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") String requesterId,
            @PathVariable(value = "filter", required = true) String filter,
            @PathVariable(value = "cid", required = true) String cid
    ) {
        log.info("Request to delete company received for cid : {}", cid);
        if(!filter.matches("(?i)cid"))
            throw new EntityServiceException("Only allowed to filter via : cid");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCid(cid);
        CreateCompanyResponseModel deletedCompanyResponseModel = modelMapper.map(companyService.deleteCompany(companyDto, requesterId), CreateCompanyResponseModel.class);
        return new ResponseEntity<>(deletedCompanyResponseModel, HttpStatus.OK);
    }

}
