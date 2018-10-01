/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CompanyComponentsPage, CompanyDeleteDialog, CompanyUpdatePage } from './company.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Company e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let companyUpdatePage: CompanyUpdatePage;
    let companyComponentsPage: CompanyComponentsPage;
    let companyDeleteDialog: CompanyDeleteDialog;
    const fileToUpload = '../../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Companies', async () => {
        await navBarPage.goToEntity('company');
        companyComponentsPage = new CompanyComponentsPage();
        expect(await companyComponentsPage.getTitle()).to.eq('invoisterApp.company.home.title');
    });

    it('should load create Company page', async () => {
        await companyComponentsPage.clickOnCreateButton();
        companyUpdatePage = new CompanyUpdatePage();
        expect(await companyUpdatePage.getPageTitle()).to.eq('invoisterApp.company.home.createOrEditLabel');
        await companyUpdatePage.cancel();
    });

    it('should create and save Companies', async () => {
        const nbButtonsBeforeCreate = await companyComponentsPage.countDeleteButtons();

        await companyComponentsPage.clickOnCreateButton();
        await companyUpdatePage.setTitleInput('title');
        expect(await companyUpdatePage.getTitleInput()).to.eq('title');
        await companyUpdatePage.setCIdInput('cId');
        expect(await companyUpdatePage.getCIdInput()).to.eq('cId');
        await companyUpdatePage.setMainPhoneNumberInput('mainPhoneNumber');
        expect(await companyUpdatePage.getMainPhoneNumberInput()).to.eq('mainPhoneNumber');
        await companyUpdatePage.setSecondaryPhoneNumberInput('secondaryPhoneNumber');
        expect(await companyUpdatePage.getSecondaryPhoneNumberInput()).to.eq('secondaryPhoneNumber');
        await companyUpdatePage.setEmailInput('email');
        expect(await companyUpdatePage.getEmailInput()).to.eq('email');
        await companyUpdatePage.setAddressInput('address');
        expect(await companyUpdatePage.getAddressInput()).to.eq('address');
        await companyUpdatePage.setPaypalAccountInput('paypalAccount');
        expect(await companyUpdatePage.getPaypalAccountInput()).to.eq('paypalAccount');
        await companyUpdatePage.setBankAccountInput('bankAccount');
        expect(await companyUpdatePage.getBankAccountInput()).to.eq('bankAccount');
        await companyUpdatePage.setLogoInput(absolutePath);
        await companyUpdatePage.setSecondaryLogoInput(absolutePath);
        await companyUpdatePage.setStampInput(absolutePath);
        await companyUpdatePage.save();
        expect(await companyUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await companyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Company', async () => {
        const nbButtonsBeforeDelete = await companyComponentsPage.countDeleteButtons();
        await companyComponentsPage.clickOnLastDeleteButton();

        companyDeleteDialog = new CompanyDeleteDialog();
        expect(await companyDeleteDialog.getDialogTitle()).to.eq('invoisterApp.company.delete.question');
        await companyDeleteDialog.clickOnConfirmButton();

        expect(await companyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
