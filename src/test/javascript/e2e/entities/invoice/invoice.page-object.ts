import { element, by, ElementFinder } from 'protractor';

export class InvoiceComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-invoice div table .btn-danger'));
    title = element.all(by.css('jhi-invoice div h2#page-heading span')).first();

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

export class InvoiceUpdatePage {
    pageTitle = element(by.id('jhi-invoice-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    dateInput = element(by.id('field_date'));
    taxInput = element(by.id('field_tax'));
    paymentFormSelect = element(by.id('field_paymentForm'));
    paymentDetailsInput = element(by.id('field_paymentDetails'));
    paymentDateInput = element(by.id('field_paymentDate'));
    statusSelect = element(by.id('field_status'));
    companySelect = element(by.id('field_company'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setDateInput(date) {
        await this.dateInput.sendKeys(date);
    }

    async getDateInput() {
        return this.dateInput.getAttribute('value');
    }

    async setTaxInput(tax) {
        await this.taxInput.sendKeys(tax);
    }

    async getTaxInput() {
        return this.taxInput.getAttribute('value');
    }

    async setPaymentFormSelect(paymentForm) {
        await this.paymentFormSelect.sendKeys(paymentForm);
    }

    async getPaymentFormSelect() {
        return this.paymentFormSelect.element(by.css('option:checked')).getText();
    }

    async paymentFormSelectLastOption() {
        await this.paymentFormSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async setPaymentDetailsInput(paymentDetails) {
        await this.paymentDetailsInput.sendKeys(paymentDetails);
    }

    async getPaymentDetailsInput() {
        return this.paymentDetailsInput.getAttribute('value');
    }

    async setPaymentDateInput(paymentDate) {
        await this.paymentDateInput.sendKeys(paymentDate);
    }

    async getPaymentDateInput() {
        return this.paymentDateInput.getAttribute('value');
    }

    async setStatusSelect(status) {
        await this.statusSelect.sendKeys(status);
    }

    async getStatusSelect() {
        return this.statusSelect.element(by.css('option:checked')).getText();
    }

    async statusSelectLastOption() {
        await this.statusSelect
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

export class InvoiceDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-invoice-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-invoice'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
