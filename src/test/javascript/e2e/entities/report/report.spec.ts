/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ReportComponentsPage, ReportDeleteDialog, ReportUpdatePage } from './report.page-object';

const expect = chai.expect;

describe('Report e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let reportUpdatePage: ReportUpdatePage;
    let reportComponentsPage: ReportComponentsPage;
    let reportDeleteDialog: ReportDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Reports', async () => {
        await navBarPage.goToEntity('report');
        reportComponentsPage = new ReportComponentsPage();
        expect(await reportComponentsPage.getTitle()).to.eq('invoisterApp.report.home.title');
    });

    it('should load create Report page', async () => {
        await reportComponentsPage.clickOnCreateButton();
        reportUpdatePage = new ReportUpdatePage();
        expect(await reportUpdatePage.getPageTitle()).to.eq('invoisterApp.report.home.createOrEditLabel');
        await reportUpdatePage.cancel();
    });

    it('should create and save Reports', async () => {
        const nbButtonsBeforeCreate = await reportComponentsPage.countDeleteButtons();

        await reportComponentsPage.clickOnCreateButton();
        await reportUpdatePage.setRIdInput('rId');
        expect(await reportUpdatePage.getRIdInput()).to.eq('rId');
        await reportUpdatePage.setDateInput('2000-12-31');
        expect(await reportUpdatePage.getDateInput()).to.eq('2000-12-31');
        await reportUpdatePage.setBodyInput('body');
        expect(await reportUpdatePage.getBodyInput()).to.eq('body');
        await reportUpdatePage.companySelectLastOption();
        await reportUpdatePage.save();
        expect(await reportUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await reportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Report', async () => {
        const nbButtonsBeforeDelete = await reportComponentsPage.countDeleteButtons();
        await reportComponentsPage.clickOnLastDeleteButton();

        reportDeleteDialog = new ReportDeleteDialog();
        expect(await reportDeleteDialog.getDialogTitle()).to.eq('invoisterApp.report.delete.question');
        await reportDeleteDialog.clickOnConfirmButton();

        expect(await reportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
