import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { WriterTools } from './writer-tools.model';
import { WriterToolsPopupService } from './writer-tools-popup.service';
import { WriterToolsService } from './writer-tools.service';
import { User, UserService } from '../../shared';
import { Board, BoardService } from '../board';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-writer-tools-dialog',
    templateUrl: './writer-tools-dialog.component.html'
})
export class WriterToolsDialogComponent implements OnInit {

    writerTools: WriterTools;
    authorities: any[];
    isSaving: boolean;

    users: User[];

    boards: Board[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private writerToolsService: WriterToolsService,
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
        if (this.writerTools.id !== undefined) {
            this.subscribeToSaveResponse(
                this.writerToolsService.update(this.writerTools), false);
        } else {
            this.subscribeToSaveResponse(
                this.writerToolsService.create(this.writerTools), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<WriterTools>, isCreated: boolean) {
        result.subscribe((res: WriterTools) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: WriterTools, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'wBoardApp.writerTools.created'
            : 'wBoardApp.writerTools.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'writerToolsListModification', content: 'OK'});
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
    selector: 'jhi-writer-tools-popup',
    template: ''
})
export class WriterToolsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private writerToolsPopupService: WriterToolsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.writerToolsPopupService
                    .open(WriterToolsDialogComponent, params['id']);
            } else {
                this.modalRef = this.writerToolsPopupService
                    .open(WriterToolsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
