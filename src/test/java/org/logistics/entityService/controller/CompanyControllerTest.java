package org.logistics.entityService.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.logistics.entityService.data.entities.CompanyEntity;
import org.logistics.entityService.data.entities.PlaceEntity;
import org.logistics.entityService.data.repositories.CompanyRepository;
import org.logistics.entityService.data.repositories.PlaceRepository;
import org.logistics.entityService.data.repositories.UsersRepository;
import org.logistics.entityService.kafka.Producer;
import org.logistics.entityService.model.data.*;
import org.logistics.entityService.model.request.CreateCompanyRequestModel;
import org.logistics.entityService.model.request.CreatePlaceRequestModel;
import org.logistics.entityService.model.request.UpdateCompanyRequestModel;
import org.logistics.entityService.model.response.CreateCompanyResponseModel;
import org.logistics.entityService.service.impl.MapService;
import org.logistics.entityService.shared.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@SpringBootTest
public class CompanyControllerTest {

    @Autowired
    private CompanyController companyController;

    @MockBean
    private Utils utils;

    @MockBean
    private CompanyRepository companyRepository;

    @MockBean
    private Producer producer;

    @MockBean
    private PlaceRepository placeRepository;

    @MockBean
    private MapService mapService;

    @Test
    public void getAllActiveCompanyTest() {
        String[] type = {"CNR"};
        when(companyRepository.findAllCompaniesWhereActiveStateIs(true))
                .thenReturn(new ArrayList<>(Arrays.asList(
                        new CompanyEntity(1L, "COM-ddb39364-23f9-4571-af60-d29d6a84bab3", "COM1", "Company Name", "GSTIN", "TIN", "TAN", "CIN", "PAN", "2123123123", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null, null, type, new Date(), new Date(), true, false, null, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null),
                        new CompanyEntity(2L, "COM-ddb39364-23f9-4571-af60-d29d6a84bab5", "COM1", "Company Name", "GSTIN", "TIN", "TAN", "CIN", "PAN", "2123123123", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null, null, type, new Date(), new Date(), true, false, null, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null)
                )));
        Assertions.assertEquals(2, ((Collection<?>) companyController.getCompany(false).getBody().getData()).size());
    }

    @Test
    public void getAllCompanyTest() {
        String[] type = {"CNR"};
        when(companyRepository.findAll())
                .thenReturn(new ArrayList<>(Arrays.asList(
                        new CompanyEntity(1L, "COM-ddb39364-23f9-4571-af60-d29d6a84bab3", "COM1", "Company Name", "GSTIN", "TIN", "TAN", "CIN", "PAN", "2123123123", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null, null, type, new Date(), new Date(), true, false, null, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null),
                        new CompanyEntity(2L, "COM-ddb39364-23f9-4571-af60-d29d6a84bab5", "COM1", "Company Name", "GSTIN", "TIN", "TAN", "CIN", "PAN", "2123123123", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null, null, type, new Date(), new Date(), false, false, null, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null)
                )));
        Assertions.assertEquals(2, ((Collection<?>) companyController.getCompany(true).getBody().getData()).size());
    }

    @Test
    public void getActiveCompanyBasedOnCidTest() {
        String[] type = {"CNR"};
        when(companyRepository.findAllCompaniesBasedOnCidAndAreActive("COM-ddb39364-23f9-4571-af60-d29d6a84bab3"))
                .thenReturn(new ArrayList<>(Arrays.asList(
                        new CompanyEntity(1L, "COM-ddb39364-23f9-4571-af60-d29d6a84bab3", "COM1", "Company Name", "GSTIN", "TIN", "TAN", "CIN", "PAN", "2123123123", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null, null, type, new Date(), new Date(), true, false, null, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null)
                )));
        final Boolean[] isActive = new Boolean[1];
        companyController.getCompanyBasedOnCid(false, "cid", "COM-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().getData().forEach(value -> isActive[0] = value.getIsActive());
        Assertions.assertEquals(1, ((Collection<?>) companyController.getCompanyBasedOnCid(false, "cid", "COM-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().getData()).size());
        Assertions.assertEquals(true, isActive[0]);
    }

    @Test
    public void getInactiveCompanyBasedOnCidTest() {
        String[] type = {"CNR"};
        when(companyRepository.findAllByCid("COM-ddb39364-23f9-4571-af60-d29d6a84bab3"))
                .thenReturn(new ArrayList<>(Arrays.asList(
                        new CompanyEntity(1L, "COM-ddb39364-23f9-4571-af60-d29d6a84bab3", "COM1", "Company Name", "GSTIN", "TIN", "TAN", "CIN", "PAN", "2123123123", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null, null, type, new Date(), new Date(), false, false, null, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null)
                )));
        final Boolean[] isActive = new Boolean[1];
        companyController.getCompanyBasedOnCid(true, "cid", "COM-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().getData().forEach(value -> isActive[0] = value.getIsActive());
        Assertions.assertEquals(1, ((Collection<?>) companyController.getCompanyBasedOnCid(true, "cid", "COM-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().getData()).size());
        Assertions.assertEquals(false, isActive[0]);
    }

    @Test
    public void getCompanyWithInvalidFilterTest() {
        try {
            companyController.getCompanyBasedOnCid(true, "pid", "COM-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (Exception ex) {
            Assertions.assertEquals("Only allowed to filter via : cid", ex.getMessage());
        }
    }

    @Test
    public void getInvalidCompanyTest() {
        try {
            companyController.getCompanyBasedOnCid(true, "cid", "COM-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (Exception ex) {
            Assertions.assertEquals("No companies found!!!", ex.getMessage());
        }
    }

    @Test
    public void createCompanyWithoutPlaceAndHeadOfficeTest() {
        String[] type = {"CNR"};
        CreateCompanyResponseModel createdCompany = companyController.createCompany(new CreateCompanyRequestModel("COM1", "Company Name", "GSTIN", "TIN", "CIN", "PAN", "TAN", type, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null, null, "2123123123"), "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody();
        Assertions.assertEquals("COM1", createdCompany.getShortCode());
        Assertions.assertEquals(true, createdCompany.getIsActive());
        Assertions.assertEquals(null, createdCompany.getPlaceId());
        Assertions.assertEquals(null, createdCompany.getHeadOfficeId());
    }

    @Test
    public void createCompanyWithPlaceAndWithoutHeadOfficeTest() {
        String[] type = {"CNR"};
        when(utils.getUniqueId())
                .thenReturn("ddb39364-23f9-4571-af60-d29d6a84bab3");
        GeocodePlaceDetails[] results = {new GeocodePlaceDetails("DummyPlaceId")};
        when(mapService.getPlaceIdBasedOnLatLong(1.222f, 3.444f))
                .thenReturn(new GetGeocodeDetailsModel(results));
        when(mapService.getPlaceDetailsBasedOnId("DummyPlaceId"))
                .thenReturn(new GetPlaceDetailsModel(
                        new GetPlaceDetailsResponseModel("adr_address", "formatted_address",
                                new GeometryData(new LocationDetails(1.222f, 3.444f), new ViewportDetails(new LocationDetails(1.222f, 3.444f), new LocationDetails(1.222f, 3.444f))), "place_id")));
        when(placeRepository.findAllActivePlacesForPid("PLC-ddb39364-23f9-4571-af60-d29d6a84bab3"))
                .thenReturn(new ArrayList<>(Arrays.asList(
                        new PlaceEntity()
                )));
        CreateCompanyResponseModel createdCompany = companyController.createCompany(new CreateCompanyRequestModel("COM1", "Company Name", "GSTIN", "TIN", "CIN", "PAN", "TAN", type, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", new CreatePlaceRequestModel("PLC", "label", "address", 1.222f, 3.444f, null), null, "2123123123"), "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody();
        Assertions.assertEquals("COM1", createdCompany.getShortCode());
        Assertions.assertEquals(true, createdCompany.getIsActive());
        Assertions.assertEquals("PLC-ddb39364-23f9-4571-af60-d29d6a84bab3", createdCompany.getPlaceId());
        Assertions.assertEquals(null, createdCompany.getHeadOfficeId());
    }

    @Test
    public void createCompanyWithoutPlaceAndWithHeadOfficeTest() {
        String[] type = {"CNR"};
        when(utils.getUniqueId())
                .thenReturn("ddb39364-23f9-4571-af60-d29d6a84bab3");
        GeocodePlaceDetails[] results = {new GeocodePlaceDetails("DummyPlaceId")};
        when(mapService.getPlaceIdBasedOnLatLong(1.222f, 3.444f))
                .thenReturn(new GetGeocodeDetailsModel(results));
        when(mapService.getPlaceDetailsBasedOnId("DummyPlaceId"))
                .thenReturn(new GetPlaceDetailsModel(
                        new GetPlaceDetailsResponseModel("adr_address", "formatted_address",
                                new GeometryData(new LocationDetails(1.222f, 3.444f), new ViewportDetails(new LocationDetails(1.222f, 3.444f), new LocationDetails(1.222f, 3.444f))), "place_id")));
        when(placeRepository.findAllActivePlacesForPid("PLC-ddb39364-23f9-4571-af60-d29d6a84bab3"))
                .thenReturn(new ArrayList<>(Arrays.asList(
                        new PlaceEntity()
                )));
        CreateCompanyResponseModel createdCompany = companyController.createCompany(new CreateCompanyRequestModel("COM1", "Company Name", "GSTIN", "TIN", "CIN", "PAN", "TAN", type, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null, new CreatePlaceRequestModel("PLC", "label", "address", 1.222f, 3.444f, null), "2123123123"), "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody();
        Assertions.assertEquals("COM1", createdCompany.getShortCode());
        Assertions.assertEquals(true, createdCompany.getIsActive());
        Assertions.assertEquals("PLC-ddb39364-23f9-4571-af60-d29d6a84bab3", createdCompany.getHeadOfficeId());
        Assertions.assertEquals(null, createdCompany.getPlaceId());
    }

    @Test
    public void createCompanyWithPlaceAndWithHeadOfficeTest() {
        String[] type = {"CNR"};
        when(utils.getUniqueId())
                .thenReturn("ddb39364-23f9-4571-af60-d29d6a84bab3");
        GeocodePlaceDetails[] results = {new GeocodePlaceDetails("DummyPlaceId")};
        when(mapService.getPlaceIdBasedOnLatLong(1.222f, 3.444f))
                .thenReturn(new GetGeocodeDetailsModel(results));
        when(mapService.getPlaceDetailsBasedOnId("DummyPlaceId"))
                .thenReturn(new GetPlaceDetailsModel(
                        new GetPlaceDetailsResponseModel("adr_address", "formatted_address",
                                new GeometryData(new LocationDetails(1.222f, 3.444f), new ViewportDetails(new LocationDetails(1.222f, 3.444f), new LocationDetails(1.222f, 3.444f))), "place_id")));
        when(placeRepository.findAllActivePlacesForPid("PLC-ddb39364-23f9-4571-af60-d29d6a84bab3"))
                .thenReturn(new ArrayList<>(Arrays.asList(
                        new PlaceEntity()
                )));
        CreateCompanyResponseModel createdCompany = companyController.createCompany(new CreateCompanyRequestModel("COM1", "Company Name", "GSTIN", "TIN", "CIN", "PAN", "TAN", type, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", new CreatePlaceRequestModel("PLC", "label", "address", 1.222f, 3.444f, null), new CreatePlaceRequestModel("PLC", "label", "address", 1.222f, 3.444f, null), "2123123123"), "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody();
        Assertions.assertEquals("COM1", createdCompany.getShortCode());
        Assertions.assertEquals(true, createdCompany.getIsActive());
        Assertions.assertEquals("PLC-ddb39364-23f9-4571-af60-d29d6a84bab3", createdCompany.getHeadOfficeId());
        Assertions.assertEquals("PLC-ddb39364-23f9-4571-af60-d29d6a84bab3", createdCompany.getPlaceId());
    }
}
