package org.logistics.entityService.service;

import org.logistics.entityService.shared.CompanyDto;

import java.util.List;

public interface CompanyService {

    CompanyDto createCompany(CompanyDto companyDto, String requesterId);

    List<CompanyDto> getAllCompanies(boolean allCompanies);

    List<CompanyDto> getAllCompaniesBasedOnCid(String cid, boolean allCompanies);

    CompanyDto updateCompany(String companyId, CompanyDto companyDto, String requesterId);

    CompanyDto updateCompanyStatus(String companyId, CompanyDto companyDto, String requesterId);

    CompanyDto deleteCompany(CompanyDto companyDto, String requesterId);
}
