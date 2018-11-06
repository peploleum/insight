import { browser, ExpectedConditions as ec, protractor } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ObservedDataComponentsPage, ObservedDataUpdatePage } from './observed-data.page-object';

describe('ObservedData e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let observedDataUpdatePage: ObservedDataUpdatePage;
    let observedDataComponentsPage: ObservedDataComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load ObservedData', async () => {
        await navBarPage.goToEntity('observed-data');
        observedDataComponentsPage = new ObservedDataComponentsPage();
        expect(await observedDataComponentsPage.getTitle()).toMatch(/insightApp.observedData.home.title/);
    });

    it('should load create ObservedData page', async () => {
        await observedDataComponentsPage.clickOnCreateButton();
        observedDataUpdatePage = new ObservedDataUpdatePage();
        expect(await observedDataUpdatePage.getPageTitle()).toMatch(/insightApp.observedData.home.createOrEditLabel/);
        await observedDataUpdatePage.cancel();
    });

    it('should create and save ObservedData', async () => {
        await observedDataComponentsPage.clickOnCreateButton();
        await observedDataUpdatePage.setTypeInput('type');
        expect(await observedDataUpdatePage.getTypeInput()).toMatch('type');
        await observedDataUpdatePage.setObjetsObservesInput('objetsObserves');
        expect(await observedDataUpdatePage.getObjetsObservesInput()).toMatch('objetsObserves');
        await observedDataUpdatePage.setDateDebutInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
        expect(await observedDataUpdatePage.getDateDebutInput()).toContain('2001-01-01T02:30');
        await observedDataUpdatePage.setDateFinInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
        expect(await observedDataUpdatePage.getDateFinInput()).toContain('2001-01-01T02:30');
        await observedDataUpdatePage.setNombreJoursInput('5');
        expect(await observedDataUpdatePage.getNombreJoursInput()).toMatch('5');
        await observedDataUpdatePage.linkOfSelectLastOption();
        await observedDataUpdatePage.save();
        expect(await observedDataUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});
