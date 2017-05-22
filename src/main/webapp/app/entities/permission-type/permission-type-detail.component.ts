import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { PermissionType } from './permission-type.model';
import { PermissionTypeService } from './permission-type.service';

@Component({
    selector: 'jhi-permission-type-detail',
    templateUrl: './permission-type-detail.component.html'
})
export class PermissionTypeDetailComponent implements OnInit, OnDestroy {

    permissionType: PermissionType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private permissionTypeService: PermissionTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPermissionTypes();
    }

    load(id) {
        this.permissionTypeService.find(id).subscribe((permissionType) => {
            this.permissionType = permissionType;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPermissionTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'permissionTypeListModification',
            (response) => this.load(this.permissionType.id)
        );
    }
}
