import { element, by, ElementFinder } from 'protractor';

export class BudgetComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-budget div table .btn-danger'));
    title = element.all(by.css('jhi-budget div h2#page-heading span')).first();

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

export class BudgetUpdatePage {
    pageTitle = element(by.id('jhi-budget-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    dateInput = element(by.id('field_date'));
    bodyInput = element(by.id('field_body'));
    grossCostInput = element(by.id('field_grossCost'));
    taxInput = element(by.id('field_tax'));
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

    async setBodyInput(body) {
        await this.bodyInput.sendKeys(body);
    }

    async getBodyInput() {
        return this.bodyInput.getAttribute('value');
    }

    async setGrossCostInput(grossCost) {
        await this.grossCostInput.sendKeys(grossCost);
    }

    async getGrossCostInput() {
        return this.grossCostInput.getAttribute('value');
    }

    async setTaxInput(tax) {
        await this.taxInput.sendKeys(tax);
    }

    async getTaxInput() {
        return this.taxInput.getAttribute('value');
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

export class BudgetDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-budget-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-budget'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
