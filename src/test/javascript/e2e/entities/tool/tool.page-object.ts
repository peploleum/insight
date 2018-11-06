import { element, by, ElementFinder } from 'protractor';

export class ToolComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-tool div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ToolUpdatePage {
    pageTitle = element(by.id('jhi-tool-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nomInput = element(by.id('field_nom'));
    typeInput = element(by.id('field_type'));
    libelleInput = element(by.id('field_libelle'));
    descriptionInput = element(by.id('field_description'));
    versionInput = element(by.id('field_version'));
    usesToolToIntrusionSetSelect = element(by.id('field_usesToolToIntrusionSet'));
    isUsesToolToMalwareSelect = element(by.id('field_isUsesToolToMalware'));
    linkOfSelect = element(by.id('field_linkOf'));
    usesToolToThreatActorSelect = element(by.id('field_usesToolToThreatActor'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNomInput(nom) {
        await this.nomInput.sendKeys(nom);
    }

    async getNomInput() {
        return this.nomInput.getAttribute('value');
    }

    async setTypeInput(type) {
        await this.typeInput.sendKeys(type);
    }

    async getTypeInput() {
        return this.typeInput.getAttribute('value');
    }

    async setLibelleInput(libelle) {
        await this.libelleInput.sendKeys(libelle);
    }

    async getLibelleInput() {
        return this.libelleInput.getAttribute('value');
    }

    async setDescriptionInput(description) {
        await this.descriptionInput.sendKeys(description);
    }

    async getDescriptionInput() {
        return this.descriptionInput.getAttribute('value');
    }

    async setVersionInput(version) {
        await this.versionInput.sendKeys(version);
    }

    async getVersionInput() {
        return this.versionInput.getAttribute('value');
    }

    async usesToolToIntrusionSetSelectLastOption() {
        await this.usesToolToIntrusionSetSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async usesToolToIntrusionSetSelectOption(option) {
        await this.usesToolToIntrusionSetSelect.sendKeys(option);
    }

    getUsesToolToIntrusionSetSelect(): ElementFinder {
        return this.usesToolToIntrusionSetSelect;
    }

    async getUsesToolToIntrusionSetSelectedOption() {
        return this.usesToolToIntrusionSetSelect.element(by.css('option:checked')).getText();
    }

    async isUsesToolToMalwareSelectLastOption() {
        await this.isUsesToolToMalwareSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async isUsesToolToMalwareSelectOption(option) {
        await this.isUsesToolToMalwareSelect.sendKeys(option);
    }

    getIsUsesToolToMalwareSelect(): ElementFinder {
        return this.isUsesToolToMalwareSelect;
    }

    async getIsUsesToolToMalwareSelectedOption() {
        return this.isUsesToolToMalwareSelect.element(by.css('option:checked')).getText();
    }

    async linkOfSelectLastOption() {
        await this.linkOfSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async linkOfSelectOption(option) {
        await this.linkOfSelect.sendKeys(option);
    }

    getLinkOfSelect(): ElementFinder {
        return this.linkOfSelect;
    }

    async getLinkOfSelectedOption() {
        return this.linkOfSelect.element(by.css('option:checked')).getText();
    }

    async usesToolToThreatActorSelectLastOption() {
        await this.usesToolToThreatActorSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async usesToolToThreatActorSelectOption(option) {
        await this.usesToolToThreatActorSelect.sendKeys(option);
    }

    getUsesToolToThreatActorSelect(): ElementFinder {
        return this.usesToolToThreatActorSelect;
    }

    async getUsesToolToThreatActorSelectedOption() {
        return this.usesToolToThreatActorSelect.element(by.css('option:checked')).getText();
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
