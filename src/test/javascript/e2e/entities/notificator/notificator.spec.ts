/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { NotificatorComponentsPage, NotificatorDeleteDialog, NotificatorUpdatePage } from './notificator.page-object';

const expect = chai.expect;

describe('Notificator e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let notificatorUpdatePage: NotificatorUpdatePage;
    let notificatorComponentsPage: NotificatorComponentsPage;
    let notificatorDeleteDialog: NotificatorDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Notificators', async () => {
        await navBarPage.goToEntity('notificator');
        notificatorComponentsPage = new NotificatorComponentsPage();
        expect(await notificatorComponentsPage.getTitle()).to.eq('invoisterApp.notificator.home.title');
    });

    it('should load create Notificator page', async () => {
        await notificatorComponentsPage.clickOnCreateButton();
        notificatorUpdatePage = new NotificatorUpdatePage();
        expect(await notificatorUpdatePage.getPageTitle()).to.eq('invoisterApp.notificator.home.createOrEditLabel');
        await notificatorUpdatePage.cancel();
    });

    it('should create and save Notificators', async () => {
        const nbButtonsBeforeCreate = await notificatorComponentsPage.countDeleteButtons();

        await notificatorComponentsPage.clickOnCreateButton();
        await notificatorUpdatePage.setSmtpHostInput('smtpHost');
        expect(await notificatorUpdatePage.getSmtpHostInput()).to.eq('smtpHost');
        await notificatorUpdatePage.setSmtpPortInput('5');
        expect(await notificatorUpdatePage.getSmtpPortInput()).to.eq('5');
        const selectedSmtpAuth = notificatorUpdatePage.getSmtpAuthInput();
        if (await selectedSmtpAuth.isSelected()) {
            await notificatorUpdatePage.getSmtpAuthInput().click();
            expect(await notificatorUpdatePage.getSmtpAuthInput().isSelected()).to.be.false;
        } else {
            await notificatorUpdatePage.getSmtpAuthInput().click();
            expect(await notificatorUpdatePage.getSmtpAuthInput().isSelected()).to.be.true;
        }
        await notificatorUpdatePage.setUsernameInput('username');
        expect(await notificatorUpdatePage.getUsernameInput()).to.eq('username');
        await notificatorUpdatePage.setPasswordInput('password');
        expect(await notificatorUpdatePage.getPasswordInput()).to.eq('password');
        await notificatorUpdatePage.secureConnectionSelectLastOption();
        await notificatorUpdatePage.companySelectLastOption();
        await notificatorUpdatePage.save();
        expect(await notificatorUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await notificatorComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Notificator', async () => {
        const nbButtonsBeforeDelete = await notificatorComponentsPage.countDeleteButtons();
        await notificatorComponentsPage.clickOnLastDeleteButton();

        notificatorDeleteDialog = new NotificatorDeleteDialog();
        expect(await notificatorDeleteDialog.getDialogTitle()).to.eq('invoisterApp.notificator.delete.question');
        await notificatorDeleteDialog.clickOnConfirmButton();

        expect(await notificatorComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
