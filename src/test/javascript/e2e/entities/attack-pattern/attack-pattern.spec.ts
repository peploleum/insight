import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { AttackPatternComponentsPage, AttackPatternUpdatePage } from './attack-pattern.page-object';

describe('AttackPattern e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let attackPatternUpdatePage: AttackPatternUpdatePage;
    let attackPatternComponentsPage: AttackPatternComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load AttackPatterns', async () => {
        await navBarPage.goToEntity('attack-pattern');
        attackPatternComponentsPage = new AttackPatternComponentsPage();
        expect(await attackPatternComponentsPage.getTitle()).toMatch(/insightApp.attackPattern.home.title/);
    });

    it('should load create AttackPattern page', async () => {
        await attackPatternComponentsPage.clickOnCreateButton();
        attackPatternUpdatePage = new AttackPatternUpdatePage();
        expect(await attackPatternUpdatePage.getPageTitle()).toMatch(/insightApp.attackPattern.home.createOrEditLabel/);
        await attackPatternUpdatePage.cancel();
    });

    it('should create and save AttackPatterns', async () => {
        await attackPatternComponentsPage.clickOnCreateButton();
        await attackPatternUpdatePage.setDescriptionInput('description');
        expect(await attackPatternUpdatePage.getDescriptionInput()).toMatch('description');
        await attackPatternUpdatePage.setNomInput('nom');
        expect(await attackPatternUpdatePage.getNomInput()).toMatch('nom');
        await attackPatternUpdatePage.setReferenceExterneInput('referenceExterne');
        expect(await attackPatternUpdatePage.getReferenceExterneInput()).toMatch('referenceExterne');
        await attackPatternUpdatePage.setTueurProcessusInput('tueurProcessus');
        expect(await attackPatternUpdatePage.getTueurProcessusInput()).toMatch('tueurProcessus');
        await attackPatternUpdatePage.setTypeInput('type');
        expect(await attackPatternUpdatePage.getTypeInput()).toMatch('type');
        await attackPatternUpdatePage.linkOfSelectLastOption();
        await attackPatternUpdatePage.save();
        expect(await attackPatternUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});
