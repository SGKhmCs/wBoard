import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Admin } from './admin.model';
import { AdminService } from './admin.service';

@Component({
    selector: 'jhi-admin-detail',
    templateUrl: './admin-detail.component.html'
})
export class AdminDetailComponent implements OnInit, OnDestroy {

    admin: Admin;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private adminService: AdminService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAdmins();
    }

    load(id) {
        this.adminService.find(id).subscribe((admin) => {
            this.admin = admin;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAdmins() {
        this.eventSubscriber = this.eventManager.subscribe(
            'adminListModification',
            (response) => this.load(this.admin.id)
        );
    }
}
