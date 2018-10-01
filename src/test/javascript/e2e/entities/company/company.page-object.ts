import { element, by, ElementFinder } from 'protractor';

export class CompanyComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-company div table .btn-danger'));
    title = element.all(by.css('jhi-company div h2#page-heading span')).first();

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

export class CompanyUpdatePage {
    pageTitle = element(by.id('jhi-company-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    titleInput = element(by.id('field_title'));
    cIdInput = element(by.id('field_cId'));
    mainPhoneNumberInput = element(by.id('field_mainPhoneNumber'));
    secondaryPhoneNumberInput = element(by.id('field_secondaryPhoneNumber'));
    emailInput = element(by.id('field_email'));
    addressInput = element(by.id('field_address'));
    paypalAccountInput = element(by.id('field_paypalAccount'));
    bankAccountInput = element(by.id('field_bankAccount'));
    logoInput = element(by.id('file_logo'));
    secondaryLogoInput = element(by.id('file_secondaryLogo'));
    stampInput = element(by.id('file_stamp'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setTitleInput(title) {
        await this.titleInput.sendKeys(title);
    }

    async getTitleInput() {
        return this.titleInput.getAttribute('value');
    }

    async setCIdInput(cId) {
        await this.cIdInput.sendKeys(cId);
    }

    async getCIdInput() {
        return this.cIdInput.getAttribute('value');
    }

    async setMainPhoneNumberInput(mainPhoneNumber) {
        await this.mainPhoneNumberInput.sendKeys(mainPhoneNumber);
    }

    async getMainPhoneNumberInput() {
        return this.mainPhoneNumberInput.getAttribute('value');
    }

    async setSecondaryPhoneNumberInput(secondaryPhoneNumber) {
        await this.secondaryPhoneNumberInput.sendKeys(secondaryPhoneNumber);
    }

    async getSecondaryPhoneNumberInput() {
        return this.secondaryPhoneNumberInput.getAttribute('value');
    }

    async setEmailInput(email) {
        await this.emailInput.sendKeys(email);
    }

    async getEmailInput() {
        return this.emailInput.getAttribute('value');
    }

    async setAddressInput(address) {
        await this.addressInput.sendKeys(address);
    }

    async getAddressInput() {
        return this.addressInput.getAttribute('value');
    }

    async setPaypalAccountInput(paypalAccount) {
        await this.paypalAccountInput.sendKeys(paypalAccount);
    }

    async getPaypalAccountInput() {
        return this.paypalAccountInput.getAttribute('value');
    }

    async setBankAccountInput(bankAccount) {
        await this.bankAccountInput.sendKeys(bankAccount);
    }

    async getBankAccountInput() {
        return this.bankAccountInput.getAttribute('value');
    }

    async setLogoInput(logo) {
        await this.logoInput.sendKeys(logo);
    }

    async getLogoInput() {
        return this.logoInput.getAttribute('value');
    }

    async setSecondaryLogoInput(secondaryLogo) {
        await this.secondaryLogoInput.sendKeys(secondaryLogo);
    }

    async getSecondaryLogoInput() {
        return this.secondaryLogoInput.getAttribute('value');
    }

    async setStampInput(stamp) {
        await this.stampInput.sendKeys(stamp);
    }

    async getStampInput() {
        return this.stampInput.getAttribute('value');
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

export class CompanyDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-company-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-company'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
