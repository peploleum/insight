import { Route } from '@angular/router';

import { EventThreadComponent } from 'app/map/event-thread.component';

export const sideRoute: Route = {
    path: 'map',
    component: EventThreadComponent,
    outlet: 'side'
};
