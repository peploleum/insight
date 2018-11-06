import { element, by, ElementFinder } from 'protractor';

export class ObservedDataComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-observed-data div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ObservedDataUpdatePage {
    pageTitle = element(by.id('jhi-observed-data-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    typeInput = element(by.id('field_type'));
    objetsObservesInput = element(by.id('field_objetsObserves'));
    dateDebutInput = element(by.id('field_dateDebut'));
    dateFinInput = element(by.id('field_dateFin'));
    nombreJoursInput = element(by.id('field_nombreJours'));
    linkOfSelect = element(by.id('field_linkOf'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setTypeInput(type) {
        await this.typeInput.sendKeys(type);
    }

    async getTypeInput() {
        return this.typeInput.getAttribute('value');
    }

    async setObjetsObservesInput(objetsObserves) {
        await this.objetsObservesInput.sendKeys(objetsObserves);
    }

    async getObjetsObservesInput() {
        return this.objetsObservesInput.getAttribute('value');
    }

    async setDateDebutInput(dateDebut) {
        await this.dateDebutInput.sendKeys(dateDebut);
    }

    async getDateDebutInput() {
        return this.dateDebutInput.getAttribute('value');
    }

    async setDateFinInput(dateFin) {
        await this.dateFinInput.sendKeys(dateFin);
    }

    async getDateFinInput() {
        return this.dateFinInput.getAttribute('value');
    }

    async setNombreJoursInput(nombreJours) {
        await this.nombreJoursInput.sendKeys(nombreJours);
    }

    async getNombreJoursInput() {
        return this.nombreJoursInput.getAttribute('value');
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
