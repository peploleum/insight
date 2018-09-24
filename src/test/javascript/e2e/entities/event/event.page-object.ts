import { element, by, ElementFinder } from 'protractor';

export class EventComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-event div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class EventUpdatePage {
    pageTitle = element(by.id('jhi-event-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    eventNameInput = element(by.id('field_eventName'));
    eventDescriptionInput = element(by.id('field_eventDescription'));
    eventTypeSelect = element(by.id('field_eventType'));
    eventCoordinatesInput = element(by.id('field_eventCoordinates'));
    eventDateInput = element(by.id('field_eventDate'));
    equipmentSelect = element(by.id('field_equipment'));
    locationSelect = element(by.id('field_location'));
    organisationSelect = element(by.id('field_organisation'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setEventNameInput(eventName) {
        await this.eventNameInput.sendKeys(eventName);
    }

    async getEventNameInput() {
        return this.eventNameInput.getAttribute('value');
    }

    async setEventDescriptionInput(eventDescription) {
        await this.eventDescriptionInput.sendKeys(eventDescription);
    }

    async getEventDescriptionInput() {
        return this.eventDescriptionInput.getAttribute('value');
    }

    async setEventTypeSelect(eventType) {
        await this.eventTypeSelect.sendKeys(eventType);
    }

    async getEventTypeSelect() {
        return this.eventTypeSelect.element(by.css('option:checked')).getText();
    }

    async eventTypeSelectLastOption() {
        await this.eventTypeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async setEventCoordinatesInput(eventCoordinates) {
        await this.eventCoordinatesInput.sendKeys(eventCoordinates);
    }

    async getEventCoordinatesInput() {
        return this.eventCoordinatesInput.getAttribute('value');
    }

    async setEventDateInput(eventDate) {
        await this.eventDateInput.sendKeys(eventDate);
    }

    async getEventDateInput() {
        return this.eventDateInput.getAttribute('value');
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
