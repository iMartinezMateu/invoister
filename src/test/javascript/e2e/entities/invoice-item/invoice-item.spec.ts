/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { InvoiceItemComponentsPage, InvoiceItemDeleteDialog, InvoiceItemUpdatePage } from './invoice-item.page-object';

const expect = chai.expect;

describe('InvoiceItem e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let invoiceItemUpdatePage: InvoiceItemUpdatePage;
    let invoiceItemComponentsPage: InvoiceItemComponentsPage;
    let invoiceItemDeleteDialog: InvoiceItemDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load InvoiceItems', async () => {
        await navBarPage.goToEntity('invoice-item');
        invoiceItemComponentsPage = new InvoiceItemComponentsPage();
        expect(await invoiceItemComponentsPage.getTitle()).to.eq('invoisterApp.invoiceItem.home.title');
    });

    it('should load create InvoiceItem page', async () => {
        await invoiceItemComponentsPage.clickOnCreateButton();
        invoiceItemUpdatePage = new InvoiceItemUpdatePage();
        expect(await invoiceItemUpdatePage.getPageTitle()).to.eq('invoisterApp.invoiceItem.home.createOrEditLabel');
        await invoiceItemUpdatePage.cancel();
    });

    it('should create and save InvoiceItems', async () => {
        const nbButtonsBeforeCreate = await invoiceItemComponentsPage.countDeleteButtons();

        await invoiceItemComponentsPage.clickOnCreateButton();
        await invoiceItemUpdatePage.setDescriptionInput('description');
        expect(await invoiceItemUpdatePage.getDescriptionInput()).to.eq('description');
        await invoiceItemUpdatePage.setQuantityInput('5');
        expect(await invoiceItemUpdatePage.getQuantityInput()).to.eq('5');
        await invoiceItemUpdatePage.setGrossCostInput('5');
        expect(await invoiceItemUpdatePage.getGrossCostInput()).to.eq('5');
        // invoiceItemUpdatePage.invoiceSelectLastOption();
        await invoiceItemUpdatePage.save();
        expect(await invoiceItemUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await invoiceItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last InvoiceItem', async () => {
        const nbButtonsBeforeDelete = await invoiceItemComponentsPage.countDeleteButtons();
        await invoiceItemComponentsPage.clickOnLastDeleteButton();

        invoiceItemDeleteDialog = new InvoiceItemDeleteDialog();
        expect(await invoiceItemDeleteDialog.getDialogTitle()).to.eq('invoisterApp.invoiceItem.delete.question');
        await invoiceItemDeleteDialog.clickOnConfirmButton();

        expect(await invoiceItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
