import { browser, ExpectedConditions as ec, protractor } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ActivityPatternComponentsPage, ActivityPatternUpdatePage } from './activity-pattern.page-object';

describe('ActivityPattern e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let activityPatternUpdatePage: ActivityPatternUpdatePage;
    let activityPatternComponentsPage: ActivityPatternComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load ActivityPatterns', async () => {
        await navBarPage.goToEntity('activity-pattern');
        activityPatternComponentsPage = new ActivityPatternComponentsPage();
        expect(await activityPatternComponentsPage.getTitle()).toMatch(/insightApp.activityPattern.home.title/);
    });

    it('should load create ActivityPattern page', async () => {
        await activityPatternComponentsPage.clickOnCreateButton();
        activityPatternUpdatePage = new ActivityPatternUpdatePage();
        expect(await activityPatternUpdatePage.getPageTitle()).toMatch(/insightApp.activityPattern.home.createOrEditLabel/);
        await activityPatternUpdatePage.cancel();
    });

    it('should create and save ActivityPatterns', async () => {
        await activityPatternComponentsPage.clickOnCreateButton();
        await activityPatternUpdatePage.setModeleInput('modele');
        expect(await activityPatternUpdatePage.getModeleInput()).toMatch('modele');
        await activityPatternUpdatePage.setNomInput('nom');
        expect(await activityPatternUpdatePage.getNomInput()).toMatch('nom');
        await activityPatternUpdatePage.setTypeInput('type');
        expect(await activityPatternUpdatePage.getTypeInput()).toMatch('type');
        await activityPatternUpdatePage.setValideAPartirDeInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
        expect(await activityPatternUpdatePage.getValideAPartirDeInput()).toContain('2001-01-01T02:30');
        await activityPatternUpdatePage.linkOfSelectLastOption();
        await activityPatternUpdatePage.save();
        expect(await activityPatternUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});
