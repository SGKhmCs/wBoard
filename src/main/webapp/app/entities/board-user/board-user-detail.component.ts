import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { BoardUser } from './board-user.model';
import { BoardUserService } from './board-user.service';

@Component({
    selector: 'jhi-board-user-detail',
    templateUrl: './board-user-detail.component.html'
})
export class BoardUserDetailComponent implements OnInit, OnDestroy {

    boardUser: BoardUser;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private boardUserService: BoardUserService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBoardUsers();
    }

    load(id) {
        this.boardUserService.find(id).subscribe((boardUser) => {
            this.boardUser = boardUser;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBoardUsers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'boardUserListModification',
            (response) => this.load(this.boardUser.id)
        );
    }
}
