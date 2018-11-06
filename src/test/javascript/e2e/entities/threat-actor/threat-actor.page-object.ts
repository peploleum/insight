import { element, by, ElementFinder } from 'protractor';

export class ThreatActorComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-threat-actor div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ThreatActorUpdatePage {
    pageTitle = element(by.id('jhi-threat-actor-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nomInput = element(by.id('field_nom'));
    typeInput = element(by.id('field_type'));
    libelleInput = element(by.id('field_libelle'));
    specificationInput = element(by.id('field_specification'));
    roleInput = element(by.id('field_role'));
    isUsesThreatActorToMalwareSelect = element(by.id('field_isUsesThreatActorToMalware'));
    linkOfSelect = element(by.id('field_linkOf'));

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

    async setSpecificationInput(specification) {
        await this.specificationInput.sendKeys(specification);
    }

    async getSpecificationInput() {
        return this.specificationInput.getAttribute('value');
    }

    async setRoleInput(role) {
        await this.roleInput.sendKeys(role);
    }

    async getRoleInput() {
        return this.roleInput.getAttribute('value');
    }

    async isUsesThreatActorToMalwareSelectLastOption() {
        await this.isUsesThreatActorToMalwareSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async isUsesThreatActorToMalwareSelectOption(option) {
        await this.isUsesThreatActorToMalwareSelect.sendKeys(option);
    }

    getIsUsesThreatActorToMalwareSelect(): ElementFinder {
        return this.isUsesThreatActorToMalwareSelect;
    }

    async getIsUsesThreatActorToMalwareSelectedOption() {
        return this.isUsesThreatActorToMalwareSelect.element(by.css('option:checked')).getText();
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
