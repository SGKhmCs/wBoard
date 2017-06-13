import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { ReaderTools } from './reader-tools.model';
import { ReaderToolsPopupService } from './reader-tools-popup.service';
import { ReaderToolsService } from './reader-tools.service';
import { User, UserService } from '../../shared';
import { Board, BoardService } from '../board';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-reader-tools-dialog',
    templateUrl: './reader-tools-dialog.component.html'
})
export class ReaderToolsDialogComponent implements OnInit {

    readerTools: ReaderTools;
    authorities: any[];
    isSaving: boolean;

    users: User[];

    boards: Board[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private readerToolsService: ReaderToolsService,
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
        if (this.readerTools.id !== undefined) {
            this.subscribeToSaveResponse(
                this.readerToolsService.update(this.readerTools), false);
        } else {
            this.subscribeToSaveResponse(
                this.readerToolsService.create(this.readerTools), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<ReaderTools>, isCreated: boolean) {
        result.subscribe((res: ReaderTools) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ReaderTools, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'wBoardApp.readerTools.created'
            : 'wBoardApp.readerTools.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'readerToolsListModification', content: 'OK'});
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
    selector: 'jhi-reader-tools-popup',
    template: ''
})
export class ReaderToolsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private readerToolsPopupService: ReaderToolsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.readerToolsPopupService
                    .open(ReaderToolsDialogComponent, params['id']);
            } else {
                this.modalRef = this.readerToolsPopupService
                    .open(ReaderToolsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
