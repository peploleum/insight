import { element, by, ElementFinder } from 'protractor';

export class EventComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('ins-event div table .btn-danger'));
    title = element.all(by.css('ins-event div h2#page-heading span')).first();

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

export class EventUpdatePage {
    pageTitle = element(by.id('ins-event-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    eventNameInput = element(by.id('field_eventName'));
    eventDescriptionInput = element(by.id('field_eventDescription'));
    eventTypeSelect = element(by.id('field_eventType'));
    eventDateInput = element(by.id('field_eventDate'));
    eventCoordinatesInput = element(by.id('field_eventCoordinates'));
    eventImageInput = element(by.id('file_eventImage'));
    eventSymbolInput = element(by.id('field_eventSymbol'));

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

    async setEventDateInput(eventDate) {
        await this.eventDateInput.sendKeys(eventDate);
    }

    async getEventDateInput() {
        return this.eventDateInput.getAttribute('value');
    }

    async setEventCoordinatesInput(eventCoordinates) {
        await this.eventCoordinatesInput.sendKeys(eventCoordinates);
    }

    async getEventCoordinatesInput() {
        return this.eventCoordinatesInput.getAttribute('value');
    }

    async setEventImageInput(eventImage) {
        await this.eventImageInput.sendKeys(eventImage);
    }

    async getEventImageInput() {
        return this.eventImageInput.getAttribute('value');
    }

    async setEventSymbolInput(eventSymbol) {
        await this.eventSymbolInput.sendKeys(eventSymbol);
    }

    async getEventSymbolInput() {
        return this.eventSymbolInput.getAttribute('value');
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

export class EventDeleteDialog {
    private dialogTitle = element(by.id('ins-delete-event-heading'));
    private confirmButton = element(by.id('ins-confirm-delete-event'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
