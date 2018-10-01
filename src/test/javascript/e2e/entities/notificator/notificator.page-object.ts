import { element, by, ElementFinder } from 'protractor';

export class NotificatorComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-notificator div table .btn-danger'));
    title = element.all(by.css('jhi-notificator div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class NotificatorUpdatePage {
    pageTitle = element(by.id('jhi-notificator-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    smtpHostInput = element(by.id('field_smtpHost'));
    smtpPortInput = element(by.id('field_smtpPort'));
    smtpAuthInput = element(by.id('field_smtpAuth'));
    usernameInput = element(by.id('field_username'));
    passwordInput = element(by.id('field_password'));
    secureConnectionSelect = element(by.id('field_secureConnection'));
    companySelect = element(by.id('field_company'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setSmtpHostInput(smtpHost) {
        await this.smtpHostInput.sendKeys(smtpHost);
    }

    async getSmtpHostInput() {
        return this.smtpHostInput.getAttribute('value');
    }

    async setSmtpPortInput(smtpPort) {
        await this.smtpPortInput.sendKeys(smtpPort);
    }

    async getSmtpPortInput() {
        return this.smtpPortInput.getAttribute('value');
    }

    getSmtpAuthInput() {
        return this.smtpAuthInput;
    }
    async setUsernameInput(username) {
        await this.usernameInput.sendKeys(username);
    }

    async getUsernameInput() {
        return this.usernameInput.getAttribute('value');
    }

    async setPasswordInput(password) {
        await this.passwordInput.sendKeys(password);
    }

    async getPasswordInput() {
        return this.passwordInput.getAttribute('value');
    }

    async setSecureConnectionSelect(secureConnection) {
        await this.secureConnectionSelect.sendKeys(secureConnection);
    }

    async getSecureConnectionSelect() {
        return this.secureConnectionSelect.element(by.css('option:checked')).getText();
    }

    async secureConnectionSelectLastOption() {
        await this.secureConnectionSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async companySelectLastOption() {
        await this.companySelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async companySelectOption(option) {
        await this.companySelect.sendKeys(option);
    }

    getCompanySelect(): ElementFinder {
        return this.companySelect;
    }

    async getCompanySelectedOption() {
        return this.companySelect.element(by.css('option:checked')).getText();
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class NotificatorDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-notificator-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-notificator'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
