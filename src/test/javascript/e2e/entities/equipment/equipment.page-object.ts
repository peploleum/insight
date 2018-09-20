import { element, by, ElementFinder } from 'protractor';

export class EquipmentComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-equipment div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class EquipmentUpdatePage {
    pageTitle = element(by.id('jhi-equipment-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    equipmentNameInput = element(by.id('field_equipmentName'));
    equipmentDescriptionInput = element(by.id('field_equipmentDescription'));
    equipmentTypeSelect = element(by.id('field_equipmentType'));
    equipmentCoordinatesInput = element(by.id('field_equipmentCoordinates'));
    locationSelect = element(by.id('field_location'));
    organisationSelect = element(by.id('field_organisation'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setEquipmentNameInput(equipmentName) {
        await this.equipmentNameInput.sendKeys(equipmentName);
    }

    async getEquipmentNameInput() {
        return this.equipmentNameInput.getAttribute('value');
    }

    async setEquipmentDescriptionInput(equipmentDescription) {
        await this.equipmentDescriptionInput.sendKeys(equipmentDescription);
    }

    async getEquipmentDescriptionInput() {
        return this.equipmentDescriptionInput.getAttribute('value');
    }

    async setEquipmentTypeSelect(equipmentType) {
        await this.equipmentTypeSelect.sendKeys(equipmentType);
    }

    async getEquipmentTypeSelect() {
        return this.equipmentTypeSelect.element(by.css('option:checked')).getText();
    }

    async equipmentTypeSelectLastOption() {
        await this.equipmentTypeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async setEquipmentCoordinatesInput(equipmentCoordinates) {
        await this.equipmentCoordinatesInput.sendKeys(equipmentCoordinates);
    }

    async getEquipmentCoordinatesInput() {
        return this.equipmentCoordinatesInput.getAttribute('value');
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
