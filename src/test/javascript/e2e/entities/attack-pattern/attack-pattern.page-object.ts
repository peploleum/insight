import { element, by, ElementFinder } from 'protractor';

export class AttackPatternComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-attack-pattern div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class AttackPatternUpdatePage {
    pageTitle = element(by.id('jhi-attack-pattern-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    descriptionInput = element(by.id('field_description'));
    nomInput = element(by.id('field_nom'));
    referenceExterneInput = element(by.id('field_referenceExterne'));
    tueurProcessusInput = element(by.id('field_tueurProcessus'));
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

    async setReferenceExterneInput(referenceExterne) {
        await this.referenceExterneInput.sendKeys(referenceExterne);
    }

    async getReferenceExterneInput() {
        return this.referenceExterneInput.getAttribute('value');
    }

    async setTueurProcessusInput(tueurProcessus) {
        await this.tueurProcessusInput.sendKeys(tueurProcessus);
    }

    async getTueurProcessusInput() {
        return this.tueurProcessusInput.getAttribute('value');
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
