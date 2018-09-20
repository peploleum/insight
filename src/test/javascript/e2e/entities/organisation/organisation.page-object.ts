import { element, by, ElementFinder } from 'protractor';

export class OrganisationComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-organisation div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class OrganisationUpdatePage {
    pageTitle = element(by.id('jhi-organisation-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    organisationNameInput = element(by.id('field_organisationName'));
    organisationSizeSelect = element(by.id('field_organisationSize'));
    organisationCoordinatesInput = element(by.id('field_organisationCoordinates'));
    locationSelect = element(by.id('field_location'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setOrganisationNameInput(organisationName) {
        await this.organisationNameInput.sendKeys(organisationName);
    }

    async getOrganisationNameInput() {
        return this.organisationNameInput.getAttribute('value');
    }

    async setOrganisationSizeSelect(organisationSize) {
        await this.organisationSizeSelect.sendKeys(organisationSize);
    }

    async getOrganisationSizeSelect() {
        return this.organisationSizeSelect.element(by.css('option:checked')).getText();
    }

    async organisationSizeSelectLastOption() {
        await this.organisationSizeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async setOrganisationCoordinatesInput(organisationCoordinates) {
        await this.organisationCoordinatesInput.sendKeys(organisationCoordinates);
    }

    async getOrganisationCoordinatesInput() {
        return this.organisationCoordinatesInput.getAttribute('value');
    }

    async locationSelectLastOption() {
        await this.locationSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async locationSelectOption(option) {
        await this.locationSelect.sendKeys(option);
    }

    getLocationSelect(): ElementFinder {
        return this.locationSelect;
    }

    async getLocationSelectedOption() {
        return this.locationSelect.element(by.css('option:checked')).getText();
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
