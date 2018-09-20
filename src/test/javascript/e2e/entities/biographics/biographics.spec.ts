import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { BiographicsComponentsPage, BiographicsUpdatePage } from './biographics.page-object';
import * as path from 'path';

describe('Biographics e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let biographicsUpdatePage: BiographicsUpdatePage;
    let biographicsComponentsPage: BiographicsComponentsPage;
    const fileToUpload = '../../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Biographics', async () => {
        await navBarPage.goToEntity('biographics');
        biographicsComponentsPage = new BiographicsComponentsPage();
        expect(await biographicsComponentsPage.getTitle()).toMatch(/insightApp.biographics.home.title/);
    });

    it('should load create Biographics page', async () => {
        await biographicsComponentsPage.clickOnCreateButton();
        biographicsUpdatePage = new BiographicsUpdatePage();
        expect(await biographicsUpdatePage.getPageTitle()).toMatch(/insightApp.biographics.home.createOrEditLabel/);
        await biographicsUpdatePage.cancel();
    });

    it('should create and save Biographics', async () => {
        await biographicsComponentsPage.clickOnCreateButton();
        await biographicsUpdatePage.setBiographicsFirstnameInput('biographicsFirstname');
        expect(await biographicsUpdatePage.getBiographicsFirstnameInput()).toMatch('biographicsFirstname');
        await biographicsUpdatePage.setBiographicsNameInput('biographicsName');
        expect(await biographicsUpdatePage.getBiographicsNameInput()).toMatch('biographicsName');
        await biographicsUpdatePage.setBiographicsAgeInput('5');
        expect(await biographicsUpdatePage.getBiographicsAgeInput()).toMatch('5');
        await biographicsUpdatePage.biographicsGenderSelectLastOption();
        await biographicsUpdatePage.setBiographicsPhotoInput(absolutePath);
        await biographicsUpdatePage.setBiographicsCoordinatesInput('biographicsCoordinates');
        expect(await biographicsUpdatePage.getBiographicsCoordinatesInput()).toMatch('biographicsCoordinates');
        // biographicsUpdatePage.eventSelectLastOption();
        // biographicsUpdatePage.equipmentSelectLastOption();
        // biographicsUpdatePage.locationSelectLastOption();
        // biographicsUpdatePage.organisationSelectLastOption();
        await biographicsUpdatePage.save();
        expect(await biographicsUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});
