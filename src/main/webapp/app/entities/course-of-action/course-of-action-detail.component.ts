import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseOfAction } from 'app/shared/model/course-of-action.model';

@Component({
    selector: 'jhi-course-of-action-detail',
    templateUrl: './course-of-action-detail.component.html'
})
export class CourseOfActionDetailComponent implements OnInit {
    courseOfAction: ICourseOfAction;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ courseOfAction }) => {
            this.courseOfAction = courseOfAction;
        });
    }

    previousState() {
        window.history.back();
    }
}
