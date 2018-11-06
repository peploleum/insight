import { element, by, ElementFinder } from 'protractor';

export class IntrusionSetComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-intrusion-set div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class IntrusionSetUpdatePage {
    pageTitle = element(by.id('jhi-intrusion-set-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    descriptionInput = element(by.id('field_description'));
    nomInput = element(by.id('field_nom'));
    typeInput = element(by.id('field_type'));
    objectifInput = element(by.id('field_objectif'));
    niveauRessourceInput = element(by.id('field_niveauRessource'));
    isTargetsIntrusionSetToActorSelect = element(by.id('field_isTargetsIntrusionSetToActor'));
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

    async setTypeInput(type) {
        await this.typeInput.sendKeys(type);
    }

    async getTypeInput() {
        return this.typeInput.getAttribute('value');
    }

    async setObjectifInput(objectif) {
        await this.objectifInput.sendKeys(objectif);
    }

    async getObjectifInput() {
        return this.objectifInput.getAttribute('value');
    }

    async setNiveauRessourceInput(niveauRessource) {
        await this.niveauRessourceInput.sendKeys(niveauRessource);
    }

    async getNiveauRessourceInput() {
        return this.niveauRessourceInput.getAttribute('value');
    }

    async isTargetsIntrusionSetToActorSelectLastOption() {
        await this.isTargetsIntrusionSetToActorSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async isTargetsIntrusionSetToActorSelectOption(option) {
        await this.isTargetsIntrusionSetToActorSelect.sendKeys(option);
    }

    getIsTargetsIntrusionSetToActorSelect(): ElementFinder {
        return this.isTargetsIntrusionSetToActorSelect;
    }

    async getIsTargetsIntrusionSetToActorSelectedOption() {
        return this.isTargetsIntrusionSetToActorSelect.element(by.css('option:checked')).getText();
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
