import { element, by, ElementFinder } from 'protractor';

export class RawDataComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('ins-raw-data div table .btn-danger'));
    title = element.all(by.css('ins-raw-data div h2#page-heading span')).first();

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

export class RawDataUpdatePage {
    pageTitle = element(by.id('ins-raw-data-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    rawDataNameInput = element(by.id('field_rawDataName'));
    rawDataTypeInput = element(by.id('field_rawDataType'));
    rawDataSubTypeInput = element(by.id('field_rawDataSubType'));
    rawDataSourceNameInput = element(by.id('field_rawDataSourceName'));
    rawDataSourceUriInput = element(by.id('field_rawDataSourceUri'));
    rawDataSourceTypeInput = element(by.id('field_rawDataSourceType'));
    rawDataContentInput = element(by.id('field_rawDataContent'));
    rawDataCreationDateInput = element(by.id('field_rawDataCreationDate'));
    rawDataExtractedDateInput = element(by.id('field_rawDataExtractedDate'));
    rawDataSymbolInput = element(by.id('field_rawDataSymbol'));
    rawDataDataInput = element(by.id('file_rawDataData'));
    rawDataCoordinatesInput = element(by.id('field_rawDataCoordinates'));
    rawDataAnnotationsInput = element(by.id('field_rawDataAnnotations'));
    externalIdInput = element(by.id('field_externalId'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setRawDataNameInput(rawDataName) {
        await this.rawDataNameInput.sendKeys(rawDataName);
    }

    async getRawDataNameInput() {
        return this.rawDataNameInput.getAttribute('value');
    }

    async setRawDataTypeInput(rawDataType) {
        await this.rawDataTypeInput.sendKeys(rawDataType);
    }

    async getRawDataTypeInput() {
        return this.rawDataTypeInput.getAttribute('value');
    }

    async setRawDataSubTypeInput(rawDataSubType) {
        await this.rawDataSubTypeInput.sendKeys(rawDataSubType);
    }

    async getRawDataSubTypeInput() {
        return this.rawDataSubTypeInput.getAttribute('value');
    }

    async setRawDataSourceNameInput(rawDataSourceName) {
        await this.rawDataSourceNameInput.sendKeys(rawDataSourceName);
    }

    async getRawDataSourceNameInput() {
        return this.rawDataSourceNameInput.getAttribute('value');
    }

    async setRawDataSourceUriInput(rawDataSourceUri) {
        await this.rawDataSourceUriInput.sendKeys(rawDataSourceUri);
    }

    async getRawDataSourceUriInput() {
        return this.rawDataSourceUriInput.getAttribute('value');
    }

    async setRawDataSourceTypeInput(rawDataSourceType) {
        await this.rawDataSourceTypeInput.sendKeys(rawDataSourceType);
    }

    async getRawDataSourceTypeInput() {
        return this.rawDataSourceTypeInput.getAttribute('value');
    }

    async setRawDataContentInput(rawDataContent) {
        await this.rawDataContentInput.sendKeys(rawDataContent);
    }

    async getRawDataContentInput() {
        return this.rawDataContentInput.getAttribute('value');
    }

    async setRawDataCreationDateInput(rawDataCreationDate) {
        await this.rawDataCreationDateInput.sendKeys(rawDataCreationDate);
    }

    async getRawDataCreationDateInput() {
        return this.rawDataCreationDateInput.getAttribute('value');
    }

    async setRawDataExtractedDateInput(rawDataExtractedDate) {
        await this.rawDataExtractedDateInput.sendKeys(rawDataExtractedDate);
    }

    async getRawDataExtractedDateInput() {
        return this.rawDataExtractedDateInput.getAttribute('value');
    }

    async setRawDataSymbolInput(rawDataSymbol) {
        await this.rawDataSymbolInput.sendKeys(rawDataSymbol);
    }

    async getRawDataSymbolInput() {
        return this.rawDataSymbolInput.getAttribute('value');
    }

    async setRawDataDataInput(rawDataData) {
        await this.rawDataDataInput.sendKeys(rawDataData);
    }

    async getRawDataDataInput() {
        return this.rawDataDataInput.getAttribute('value');
    }

    async setRawDataCoordinatesInput(rawDataCoordinates) {
        await this.rawDataCoordinatesInput.sendKeys(rawDataCoordinates);
    }

    async getRawDataCoordinatesInput() {
        return this.rawDataCoordinatesInput.getAttribute('value');
    }

    async setRawDataAnnotationsInput(rawDataAnnotations) {
        await this.rawDataAnnotationsInput.sendKeys(rawDataAnnotations);
    }

    async getRawDataAnnotationsInput() {
        return this.rawDataAnnotationsInput.getAttribute('value');
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

export class RawDataDeleteDialog {
    private dialogTitle = element(by.id('ins-delete-rawData-heading'));
    private confirmButton = element(by.id('ins-confirm-delete-rawData'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
