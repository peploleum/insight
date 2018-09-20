import { element, by, ElementFinder } from 'protractor';

export class BiographicsComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-biographics div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class BiographicsUpdatePage {
    pageTitle = element(by.id('jhi-biographics-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    biographicsFirstnameInput = element(by.id('field_biographicsFirstname'));
    biographicsNameInput = element(by.id('field_biographicsName'));
    biographicsAgeInput = element(by.id('field_biographicsAge'));
    biographicsGenderSelect = element(by.id('field_biographicsGender'));
    biographicsPhotoInput = element(by.id('file_biographicsPhoto'));
    biographicsCoordinatesInput = element(by.id('field_biographicsCoordinates'));
    eventSelect = element(by.id('field_event'));
    equipmentSelect = element(by.id('field_equipment'));
    locationSelect = element(by.id('field_location'));
    organisationSelect = element(by.id('field_organisation'));

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

    async setBiographicsPhotoInput(biographicsPhoto) {
        await this.biographicsPhotoInput.sendKeys(biographicsPhoto);
    }

    async getBiographicsPhotoInput() {
        return this.biographicsPhotoInput.getAttribute('value');
    }

    async setBiographicsCoordinatesInput(biographicsCoordinates) {
        await this.biographicsCoordinatesInput.sendKeys(biographicsCoordinates);
    }

    async getBiographicsCoordinatesInput() {
        return this.biographicsCoordinatesInput.getAttribute('value');
    }

    async eventSelectLastOption() {
        await this.eventSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async eventSelectOption(option) {
        await this.eventSelect.sendKeys(option);
    }

    getEventSelect(): ElementFinder {
        return this.eventSelect;
    }

    async getEventSelectedOption() {
        return this.eventSelect.element(by.css('option:checked')).getText();
    }

    async equipmentSelectLastOption() {
        await this.equipmentSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async equipmentSelectOption(option) {
        await this.equipmentSelect.sendKeys(option);
    }

    getEquipmentSelect(): ElementFinder {
        return this.equipmentSelect;
    }

    async getEquipmentSelectedOption() {
        return this.equipmentSelect.element(by.css('option:checked')).getText();
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

    async organisationSelectLastOption() {
        await this.organisationSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async organisationSelectOption(option) {
        await this.organisationSelect.sendKeys(option);
    }

    getOrganisationSelect(): ElementFinder {
        return this.organisationSelect;
    }

    async getOrganisationSelectedOption() {
        return this.organisationSelect.element(by.css('option:checked')).getText();
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
