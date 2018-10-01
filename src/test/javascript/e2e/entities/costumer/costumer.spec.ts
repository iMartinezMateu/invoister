/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CostumerComponentsPage, CostumerDeleteDialog, CostumerUpdatePage } from './costumer.page-object';

const expect = chai.expect;

describe('Costumer e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let costumerUpdatePage: CostumerUpdatePage;
    let costumerComponentsPage: CostumerComponentsPage;
    let costumerDeleteDialog: CostumerDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Costumers', async () => {
        await navBarPage.goToEntity('costumer');
        costumerComponentsPage = new CostumerComponentsPage();
        expect(await costumerComponentsPage.getTitle()).to.eq('invoisterApp.costumer.home.title');
    });

    it('should load create Costumer page', async () => {
        await costumerComponentsPage.clickOnCreateButton();
        costumerUpdatePage = new CostumerUpdatePage();
        expect(await costumerUpdatePage.getPageTitle()).to.eq('invoisterApp.costumer.home.createOrEditLabel');
        await costumerUpdatePage.cancel();
    });

    it('should create and save Costumers', async () => {
        const nbButtonsBeforeCreate = await costumerComponentsPage.countDeleteButtons();

        await costumerComponentsPage.clickOnCreateButton();
        await costumerUpdatePage.setNameInput('name');
        expect(await costumerUpdatePage.getNameInput()).to.eq('name');
        await costumerUpdatePage.setCIdInput('cId');
        expect(await costumerUpdatePage.getCIdInput()).to.eq('cId');
        await costumerUpdatePage.setAddressInput('address');
        expect(await costumerUpdatePage.getAddressInput()).to.eq('address');
        await costumerUpdatePage.setPhoneInput('phone');
        expect(await costumerUpdatePage.getPhoneInput()).to.eq('phone');
        await costumerUpdatePage.setEmailInput('email');
        expect(await costumerUpdatePage.getEmailInput()).to.eq('email');
        await costumerUpdatePage.companySelectLastOption();
        await costumerUpdatePage.save();
        expect(await costumerUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await costumerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Costumer', async () => {
        const nbButtonsBeforeDelete = await costumerComponentsPage.countDeleteButtons();
        await costumerComponentsPage.clickOnLastDeleteButton();

        costumerDeleteDialog = new CostumerDeleteDialog();
        expect(await costumerDeleteDialog.getDialogTitle()).to.eq('invoisterApp.costumer.delete.question');
        await costumerDeleteDialog.clickOnConfirmButton();

        expect(await costumerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
