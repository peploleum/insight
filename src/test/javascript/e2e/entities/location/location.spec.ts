import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { LocationComponentsPage, LocationUpdatePage } from './location.page-object';

describe('Location e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let locationUpdatePage: LocationUpdatePage;
    let locationComponentsPage: LocationComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Locations', async () => {
        await navBarPage.goToEntity('location');
        locationComponentsPage = new LocationComponentsPage();
        expect(await locationComponentsPage.getTitle()).toMatch(/insightApp.location.home.title/);
    });

    it('should load create Location page', async () => {
        await locationComponentsPage.clickOnCreateButton();
        locationUpdatePage = new LocationUpdatePage();
        expect(await locationUpdatePage.getPageTitle()).toMatch(/insightApp.location.home.createOrEditLabel/);
        await locationUpdatePage.cancel();
    });

    it('should create and save Locations', async () => {
        await locationComponentsPage.clickOnCreateButton();
        await locationUpdatePage.setLocationNameInput('locationName');
        expect(await locationUpdatePage.getLocationNameInput()).toMatch('locationName');
        await locationUpdatePage.locationTypeSelectLastOption();
        await locationUpdatePage.setLocationCoordinatesInput('locationCoordinates');
        expect(await locationUpdatePage.getLocationCoordinatesInput()).toMatch('locationCoordinates');
        await locationUpdatePage.save();
        expect(await locationUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});
