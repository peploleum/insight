import { element, by, ElementFinder } from 'protractor';

export class OrganisationComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('ins-organisation div table .btn-danger'));
    title = element.all(by.css('ins-organisation div h2#page-heading span')).first();

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

export class OrganisationUpdatePage {
    pageTitle = element(by.id('ins-organisation-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    organisationNameInput = element(by.id('field_organisationName'));
    organisationDescrptionInput = element(by.id('field_organisationDescrption'));
    organisationSizeSelect = element(by.id('field_organisationSize'));
    organisationCoordinatesInput = element(by.id('field_organisationCoordinates'));
    organisationImageInput = element(by.id('file_organisationImage'));
    organisationSymbolInput = element(by.id('field_organisationSymbol'));
    externalIdInput = element(by.id('field_externalId'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setOrganisationNameInput(organisationName) {
        await this.organisationNameInput.sendKeys(organisationName);
    }

    async getOrganisationNameInput() {
        return this.organisationNameInput.getAttribute('value');
    }

    async setOrganisationDescrptionInput(organisationDescrption) {
        await this.organisationDescrptionInput.sendKeys(organisationDescrption);
    }

    async getOrganisationDescrptionInput() {
        return this.organisationDescrptionInput.getAttribute('value');
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

    async setOrganisationImageInput(organisationImage) {
        await this.organisationImageInput.sendKeys(organisationImage);
    }

    async getOrganisationImageInput() {
        return this.organisationImageInput.getAttribute('value');
    }

    async setOrganisationSymbolInput(organisationSymbol) {
        await this.organisationSymbolInput.sendKeys(organisationSymbol);
    }

    async getOrganisationSymbolInput() {
        return this.organisationSymbolInput.getAttribute('value');
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

export class OrganisationDeleteDialog {
    private dialogTitle = element(by.id('ins-delete-organisation-heading'));
    private confirmButton = element(by.id('ins-confirm-delete-organisation'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
