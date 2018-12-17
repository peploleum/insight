import { element, by, ElementFinder } from 'protractor';

export class EquipmentComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('ins-equipment div table .btn-danger'));
    title = element.all(by.css('ins-equipment div h2#page-heading span')).first();

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

export class EquipmentUpdatePage {
    pageTitle = element(by.id('ins-equipment-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    equipmentNameInput = element(by.id('field_equipmentName'));
    equipmentDescriptionInput = element(by.id('field_equipmentDescription'));
    equipmentTypeSelect = element(by.id('field_equipmentType'));
    equipmentCoordinatesInput = element(by.id('field_equipmentCoordinates'));
    equipmentSymbolInput = element(by.id('field_equipmentSymbol'));
    equipmentImageInput = element(by.id('file_equipmentImage'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setEquipmentNameInput(equipmentName) {
        await this.equipmentNameInput.sendKeys(equipmentName);
    }

    async getEquipmentNameInput() {
        return this.equipmentNameInput.getAttribute('value');
    }

    async setEquipmentDescriptionInput(equipmentDescription) {
        await this.equipmentDescriptionInput.sendKeys(equipmentDescription);
    }

    async getEquipmentDescriptionInput() {
        return this.equipmentDescriptionInput.getAttribute('value');
    }

    async setEquipmentTypeSelect(equipmentType) {
        await this.equipmentTypeSelect.sendKeys(equipmentType);
    }

    async getEquipmentTypeSelect() {
        return this.equipmentTypeSelect.element(by.css('option:checked')).getText();
    }

    async equipmentTypeSelectLastOption() {
        await this.equipmentTypeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async setEquipmentCoordinatesInput(equipmentCoordinates) {
        await this.equipmentCoordinatesInput.sendKeys(equipmentCoordinates);
    }

    async getEquipmentCoordinatesInput() {
        return this.equipmentCoordinatesInput.getAttribute('value');
    }

    async setEquipmentSymbolInput(equipmentSymbol) {
        await this.equipmentSymbolInput.sendKeys(equipmentSymbol);
    }

    async getEquipmentSymbolInput() {
        return this.equipmentSymbolInput.getAttribute('value');
    }

    async setEquipmentImageInput(equipmentImage) {
        await this.equipmentImageInput.sendKeys(equipmentImage);
    }

    async getEquipmentImageInput() {
        return this.equipmentImageInput.getAttribute('value');
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

export class EquipmentDeleteDialog {
    private dialogTitle = element(by.id('ins-delete-equipment-heading'));
    private confirmButton = element(by.id('ins-confirm-delete-equipment'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
