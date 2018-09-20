import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { EquipmentComponentsPage, EquipmentUpdatePage } from './equipment.page-object';

describe('Equipment e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let equipmentUpdatePage: EquipmentUpdatePage;
    let equipmentComponentsPage: EquipmentComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Equipment', async () => {
        await navBarPage.goToEntity('equipment');
        equipmentComponentsPage = new EquipmentComponentsPage();
        expect(await equipmentComponentsPage.getTitle()).toMatch(/insightApp.equipment.home.title/);
    });

    it('should load create Equipment page', async () => {
        await equipmentComponentsPage.clickOnCreateButton();
        equipmentUpdatePage = new EquipmentUpdatePage();
        expect(await equipmentUpdatePage.getPageTitle()).toMatch(/insightApp.equipment.home.createOrEditLabel/);
        await equipmentUpdatePage.cancel();
    });

    it('should create and save Equipment', async () => {
        await equipmentComponentsPage.clickOnCreateButton();
        await equipmentUpdatePage.setEquipmentNameInput('equipmentName');
        expect(await equipmentUpdatePage.getEquipmentNameInput()).toMatch('equipmentName');
        await equipmentUpdatePage.setEquipmentDescriptionInput('equipmentDescription');
        expect(await equipmentUpdatePage.getEquipmentDescriptionInput()).toMatch('equipmentDescription');
        await equipmentUpdatePage.equipmentTypeSelectLastOption();
        await equipmentUpdatePage.setEquipmentCoordinatesInput('equipmentCoordinates');
        expect(await equipmentUpdatePage.getEquipmentCoordinatesInput()).toMatch('equipmentCoordinates');
        // equipmentUpdatePage.locationSelectLastOption();
        // equipmentUpdatePage.organisationSelectLastOption();
        await equipmentUpdatePage.save();
        expect(await equipmentUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});
