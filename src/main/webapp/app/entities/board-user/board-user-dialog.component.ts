import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { BoardUser } from './board-user.model';
import { BoardUserPopupService } from './board-user-popup.service';
import { BoardUserService } from './board-user.service';
import { User, UserService } from '../../shared';
import { Board, BoardService } from '../board';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-board-user-dialog',
    templateUrl: './board-user-dialog.component.html'
})
export class BoardUserDialogComponent implements OnInit {

    boardUser: BoardUser;
    authorities: any[];
    isSaving: boolean;

    users: User[];

    boards: Board[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private boardUserService: BoardUserService,
        private userService: UserService,
        private boardService: BoardService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.boardService.query()
            .subscribe((res: ResponseWrapper) => { this.boards = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.boardUser.id !== undefined) {
            this.subscribeToSaveResponse(
                this.boardUserService.update(this.boardUser));
        } else {
            this.subscribeToSaveResponse(
                this.boardUserService.create(this.boardUser));
        }
    }

    private subscribeToSaveResponse(result: Observable<BoardUser>) {
        result.subscribe((res: BoardUser) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: BoardUser) {
        this.eventManager.broadcast({ name: 'boardUserListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackBoardById(index: number, item: Board) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-board-user-popup',
    template: ''
})
export class BoardUserPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private boardUserPopupService: BoardUserPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.boardUserPopupService
                    .open(BoardUserDialogComponent, params['id']);
            } else {
                this.modalRef = this.boardUserPopupService
                    .open(BoardUserDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
