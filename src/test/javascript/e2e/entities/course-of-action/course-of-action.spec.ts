import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CourseOfActionComponentsPage, CourseOfActionUpdatePage } from './course-of-action.page-object';

describe('CourseOfAction e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let courseOfActionUpdatePage: CourseOfActionUpdatePage;
    let courseOfActionComponentsPage: CourseOfActionComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load CourseOfActions', async () => {
        await navBarPage.goToEntity('course-of-action');
        courseOfActionComponentsPage = new CourseOfActionComponentsPage();
        expect(await courseOfActionComponentsPage.getTitle()).toMatch(/insightApp.courseOfAction.home.title/);
    });

    it('should load create CourseOfAction page', async () => {
        await courseOfActionComponentsPage.clickOnCreateButton();
        courseOfActionUpdatePage = new CourseOfActionUpdatePage();
        expect(await courseOfActionUpdatePage.getPageTitle()).toMatch(/insightApp.courseOfAction.home.createOrEditLabel/);
        await courseOfActionUpdatePage.cancel();
    });

    it('should create and save CourseOfActions', async () => {
        await courseOfActionComponentsPage.clickOnCreateButton();
        await courseOfActionUpdatePage.setDescriptionInput('description');
        expect(await courseOfActionUpdatePage.getDescriptionInput()).toMatch('description');
        await courseOfActionUpdatePage.setNomInput('nom');
        expect(await courseOfActionUpdatePage.getNomInput()).toMatch('nom');
        await courseOfActionUpdatePage.setTypeInput('type');
        expect(await courseOfActionUpdatePage.getTypeInput()).toMatch('type');
        await courseOfActionUpdatePage.linkOfSelectLastOption();
        await courseOfActionUpdatePage.save();
        expect(await courseOfActionUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});
