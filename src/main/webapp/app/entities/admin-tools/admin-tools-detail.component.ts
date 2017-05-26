import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { AdminTools } from './admin-tools.model';
import { AdminToolsService } from './admin-tools.service';

@Component({
    selector: 'jhi-admin-tools-detail',
    templateUrl: './admin-tools-detail.component.html'
})
export class AdminToolsDetailComponent implements OnInit, OnDestroy {

    adminTools: AdminTools;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private adminToolsService: AdminToolsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAdminTools();
    }

    load(id) {
        this.adminToolsService.find(id).subscribe((adminTools) => {
            this.adminTools = adminTools;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAdminTools() {
        this.eventSubscriber = this.eventManager.subscribe(
            'adminToolsListModification',
            (response) => this.load(this.adminTools.id)
        );
    }
}
