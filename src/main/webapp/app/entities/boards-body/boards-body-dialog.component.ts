import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { BoardsBody } from './boards-body.model';
import { BoardsBodyPopupService } from './boards-body-popup.service';
import { BoardsBodyService } from './boards-body.service';
import { Board, BoardService } from '../board';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-boards-body-dialog',
    templateUrl: './boards-body-dialog.component.html'
})
export class BoardsBodyDialogComponent implements OnInit {

    boardsBody: BoardsBody;
    authorities: any[];
    isSaving: boolean;

    boards: Board[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private boardsBodyService: BoardsBodyService,
        private boardService: BoardService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.boardService
            .query({filter: 'boardsbody-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.boardsBody.boardId) {
                    this.boards = res.json;
                } else {
                    this.boardService
                        .find(this.boardsBody.boardId)
                        .subscribe((subRes: Board) => {
                            this.boards = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.boardsBody.id !== undefined) {
            this.subscribeToSaveResponse(
                this.boardsBodyService.update(this.boardsBody), false);
        } else {
            this.subscribeToSaveResponse(
                this.boardsBodyService.create(this.boardsBody), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<BoardsBody>, isCreated: boolean) {
        result.subscribe((res: BoardsBody) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: BoardsBody, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'wBoardApp.boardsBody.created'
            : 'wBoardApp.boardsBody.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'boardsBodyListModification', content: 'OK'});
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

    trackBoardById(index: number, item: Board) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-boards-body-popup',
    template: ''
})
export class BoardsBodyPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private boardsBodyPopupService: BoardsBodyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.boardsBodyPopupService
                    .open(BoardsBodyDialogComponent, params['id']);
            } else {
                this.modalRef = this.boardsBodyPopupService
                    .open(BoardsBodyDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
