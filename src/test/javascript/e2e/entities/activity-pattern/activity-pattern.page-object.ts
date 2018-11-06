import { element, by, ElementFinder } from 'protractor';

export class ActivityPatternComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-activity-pattern div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ActivityPatternUpdatePage {
    pageTitle = element(by.id('jhi-activity-pattern-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    modeleInput = element(by.id('field_modele'));
    nomInput = element(by.id('field_nom'));
    typeInput = element(by.id('field_type'));
    valideAPartirDeInput = element(by.id('field_valideAPartirDe'));
    linkOfSelect = element(by.id('field_linkOf'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setModeleInput(modele) {
        await this.modeleInput.sendKeys(modele);
    }

    async getModeleInput() {
        return this.modeleInput.getAttribute('value');
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

    async setValideAPartirDeInput(valideAPartirDe) {
        await this.valideAPartirDeInput.sendKeys(valideAPartirDe);
    }

    async getValideAPartirDeInput() {
        return this.valideAPartirDeInput.getAttribute('value');
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
