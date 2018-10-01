/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { InvoiceComponentsPage, InvoiceDeleteDialog, InvoiceUpdatePage } from './invoice.page-object';

const expect = chai.expect;

describe('Invoice e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let invoiceUpdatePage: InvoiceUpdatePage;
    let invoiceComponentsPage: InvoiceComponentsPage;
    let invoiceDeleteDialog: InvoiceDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Invoices', async () => {
        await navBarPage.goToEntity('invoice');
        invoiceComponentsPage = new InvoiceComponentsPage();
        expect(await invoiceComponentsPage.getTitle()).to.eq('invoisterApp.invoice.home.title');
    });

    it('should load create Invoice page', async () => {
        await invoiceComponentsPage.clickOnCreateButton();
        invoiceUpdatePage = new InvoiceUpdatePage();
        expect(await invoiceUpdatePage.getPageTitle()).to.eq('invoisterApp.invoice.home.createOrEditLabel');
        await invoiceUpdatePage.cancel();
    });

    it('should create and save Invoices', async () => {
        const nbButtonsBeforeCreate = await invoiceComponentsPage.countDeleteButtons();

        await invoiceComponentsPage.clickOnCreateButton();
        await invoiceUpdatePage.setDateInput('2000-12-31');
        expect(await invoiceUpdatePage.getDateInput()).to.eq('2000-12-31');
        await invoiceUpdatePage.setTaxInput('5');
        expect(await invoiceUpdatePage.getTaxInput()).to.eq('5');
        await invoiceUpdatePage.paymentFormSelectLastOption();
        await invoiceUpdatePage.setPaymentDetailsInput('paymentDetails');
        expect(await invoiceUpdatePage.getPaymentDetailsInput()).to.eq('paymentDetails');
        await invoiceUpdatePage.setPaymentDateInput('2000-12-31');
        expect(await invoiceUpdatePage.getPaymentDateInput()).to.eq('2000-12-31');
        await invoiceUpdatePage.statusSelectLastOption();
        await invoiceUpdatePage.companySelectLastOption();
        await invoiceUpdatePage.save();
        expect(await invoiceUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await invoiceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Invoice', async () => {
        const nbButtonsBeforeDelete = await invoiceComponentsPage.countDeleteButtons();
        await invoiceComponentsPage.clickOnLastDeleteButton();

        invoiceDeleteDialog = new InvoiceDeleteDialog();
        expect(await invoiceDeleteDialog.getDialogTitle()).to.eq('invoisterApp.invoice.delete.question');
        await invoiceDeleteDialog.clickOnConfirmButton();

        expect(await invoiceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
