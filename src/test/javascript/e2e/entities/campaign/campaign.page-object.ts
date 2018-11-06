import { element, by, ElementFinder } from 'protractor';

export class CampaignComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-campaign div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CampaignUpdatePage {
    pageTitle = element(by.id('jhi-campaign-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    descriptionInput = element(by.id('field_description'));
    nomInput = element(by.id('field_nom'));
    objectifInput = element(by.id('field_objectif'));
    aliasInput = element(by.id('field_alias'));
    typeInput = element(by.id('field_type'));
    linkOfSelect = element(by.id('field_linkOf'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setDescriptionInput(description) {
        await this.descriptionInput.sendKeys(description);
    }

    async getDescriptionInput() {
        return this.descriptionInput.getAttribute('value');
    }

    async setNomInput(nom) {
        await this.nomInput.sendKeys(nom);
    }

    async getNomInput() {
        return this.nomInput.getAttribute('value');
    }

    async setObjectifInput(objectif) {
        await this.objectifInput.sendKeys(objectif);
    }

    async getObjectifInput() {
        return this.objectifInput.getAttribute('value');
    }

    async setAliasInput(alias) {
        await this.aliasInput.sendKeys(alias);
    }

    async getAliasInput() {
        return this.aliasInput.getAttribute('value');
    }

    async setTypeInput(type) {
        await this.typeInput.sendKeys(type);
    }

    async getTypeInput() {
        return this.typeInput.getAttribute('value');
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
