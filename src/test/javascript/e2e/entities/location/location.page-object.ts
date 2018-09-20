import { element, by, ElementFinder } from 'protractor';

export class LocationComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-location div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class LocationUpdatePage {
    pageTitle = element(by.id('jhi-location-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    locationNameInput = element(by.id('field_locationName'));
    locationTypeSelect = element(by.id('field_locationType'));
    locationCoordinatesInput = element(by.id('field_locationCoordinates'));

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
