import { element, by, ElementFinder } from 'protractor';

export class BiographicsComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('ins-biographics div table .btn-danger'));
    title = element.all(by.css('ins-biographics div h2#page-heading span')).first();

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

export class BiographicsUpdatePage {
    pageTitle = element(by.id('ins-biographics-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    biographicsFirstnameInput = element(by.id('field_biographicsFirstname'));
    biographicsNameInput = element(by.id('field_biographicsName'));
    biographicsAgeInput = element(by.id('field_biographicsAge'));
    biographicsGenderSelect = element(by.id('field_biographicsGender'));
    biographicsImageInput = element(by.id('file_biographicsImage'));
    biographicsCoordinatesInput = element(by.id('field_biographicsCoordinates'));
    biographicsSymbolInput = element(by.id('field_biographicsSymbol'));
    externalIdInput = element(by.id('field_externalId'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setBiographicsFirstnameInput(biographicsFirstname) {
        await this.biographicsFirstnameInput.sendKeys(biographicsFirstname);
    }

    async getBiographicsFirstnameInput() {
        return this.biographicsFirstnameInput.getAttribute('value');
    }

    async setBiographicsNameInput(biographicsName) {
        await this.biographicsNameInput.sendKeys(biographicsName);
    }

    async getBiographicsNameInput() {
        return this.biographicsNameInput.getAttribute('value');
    }

    async setBiographicsAgeInput(biographicsAge) {
        await this.biographicsAgeInput.sendKeys(biographicsAge);
    }

    async getBiographicsAgeInput() {
        return this.biographicsAgeInput.getAttribute('value');
    }

    async setBiographicsGenderSelect(biographicsGender) {
        await this.biographicsGenderSelect.sendKeys(biographicsGender);
    }

    async getBiographicsGenderSelect() {
        return this.biographicsGenderSelect.element(by.css('option:checked')).getText();
    }

    async biographicsGenderSelectLastOption() {
        await this.biographicsGenderSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async setBiographicsImageInput(biographicsImage) {
        await this.biographicsImageInput.sendKeys(biographicsImage);
    }

    async getBiographicsImageInput() {
        return this.biographicsImageInput.getAttribute('value');
    }

    async setBiographicsCoordinatesInput(biographicsCoordinates) {
        await this.biographicsCoordinatesInput.sendKeys(biographicsCoordinates);
    }

    async getBiographicsCoordinatesInput() {
        return this.biographicsCoordinatesInput.getAttribute('value');
    }

    async setBiographicsSymbolInput(biographicsSymbol) {
        await this.biographicsSymbolInput.sendKeys(biographicsSymbol);
    }

    async getBiographicsSymbolInput() {
        return this.biographicsSymbolInput.getAttribute('value');
    }

    async setExternalIdInput(externalId) {
        await this.externalIdInput.sendKeys(externalId);
    }

    async getExternalIdInput() {
        return this.externalIdInput.getAttribute('value');
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

export class BiographicsDeleteDialog {
    private dialogTitle = element(by.id('ins-delete-biographics-heading'));
    private confirmButton = element(by.id('ins-confirm-delete-biographics'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
