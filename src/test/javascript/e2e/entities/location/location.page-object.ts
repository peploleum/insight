import { element, by, ElementFinder } from 'protractor';

export class LocationComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('ins-location div table .btn-danger'));
    title = element.all(by.css('ins-location div h2#page-heading span')).first();

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

export class LocationUpdatePage {
    pageTitle = element(by.id('ins-location-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    locationNameInput = element(by.id('field_locationName'));
    locationTypeSelect = element(by.id('field_locationType'));
    locationCoordinatesInput = element(by.id('field_locationCoordinates'));
    locationImageInput = element(by.id('file_locationImage'));
    locationSymbolInput = element(by.id('field_locationSymbol'));
    externalIdInput = element(by.id('field_externalId'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setLocationNameInput(locationName) {
        await this.locationNameInput.sendKeys(locationName);
    }

    async getLocationNameInput() {
        return this.locationNameInput.getAttribute('value');
    }

    async setLocationTypeSelect(locationType) {
        await this.locationTypeSelect.sendKeys(locationType);
    }

    async getLocationTypeSelect() {
        return this.locationTypeSelect.element(by.css('option:checked')).getText();
    }

    async locationTypeSelectLastOption() {
        await this.locationTypeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async setLocationCoordinatesInput(locationCoordinates) {
        await this.locationCoordinatesInput.sendKeys(locationCoordinates);
    }

    async getLocationCoordinatesInput() {
        return this.locationCoordinatesInput.getAttribute('value');
    }

    async setLocationImageInput(locationImage) {
        await this.locationImageInput.sendKeys(locationImage);
    }

    async getLocationImageInput() {
        return this.locationImageInput.getAttribute('value');
    }

    async setLocationSymbolInput(locationSymbol) {
        await this.locationSymbolInput.sendKeys(locationSymbol);
    }

    async getLocationSymbolInput() {
        return this.locationSymbolInput.getAttribute('value');
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

export class LocationDeleteDialog {
    private dialogTitle = element(by.id('ins-delete-location-heading'));
    private confirmButton = element(by.id('ins-confirm-delete-location'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
