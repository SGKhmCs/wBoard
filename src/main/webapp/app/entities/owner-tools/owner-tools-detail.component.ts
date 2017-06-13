import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { OwnerTools } from './owner-tools.model';
import { OwnerToolsService } from './owner-tools.service';

@Component({
    selector: 'jhi-owner-tools-detail',
    templateUrl: './owner-tools-detail.component.html'
})
export class OwnerToolsDetailComponent implements OnInit, OnDestroy {

    ownerTools: OwnerTools;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private ownerToolsService: OwnerToolsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInOwnerTools();
    }

    load(id) {
        this.ownerToolsService.find(id).subscribe((ownerTools) => {
            this.ownerTools = ownerTools;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInOwnerTools() {
        this.eventSubscriber = this.eventManager.subscribe(
            'ownerToolsListModification',
            (response) => this.load(this.ownerTools.id)
        );
    }
}
