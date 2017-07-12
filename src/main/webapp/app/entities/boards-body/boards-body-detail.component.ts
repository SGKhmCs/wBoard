import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { BoardsBody } from './boards-body.model';
import { BoardsBodyService } from './boards-body.service';

@Component({
    selector: 'jhi-boards-body-detail',
    templateUrl: './boards-body-detail.component.html'
})
export class BoardsBodyDetailComponent implements OnInit, OnDestroy {

    boardsBody: BoardsBody;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private boardsBodyService: BoardsBodyService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBoardsBodies();
    }

    load(id) {
        this.boardsBodyService.find(id).subscribe((boardsBody) => {
            this.boardsBody = boardsBody;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBoardsBodies() {
        this.eventSubscriber = this.eventManager.subscribe(
            'boardsBodyListModification',
            (response) => this.load(this.boardsBody.id)
        );
    }
}
