import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Reader } from './reader.model';
import { ReaderPopupService } from './reader-popup.service';
import { ReaderService } from './reader.service';
import { Board, BoardService } from '../board';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-reader-dialog',
    templateUrl: './reader-dialog.component.html'
})
export class ReaderDialogComponent implements OnInit {

    reader: Reader;
    authorities: any[];
    isSaving: boolean;

    boards: Board[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private readerService: ReaderService,
        private boardService: BoardService,
        private userService: UserService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.boardService.query()
            .subscribe((res: ResponseWrapper) => { this.boards = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.reader.id !== undefined) {
            this.subscribeToSaveResponse(
                this.readerService.update(this.reader));
        } else {
            this.subscribeToSaveResponse(
                this.readerService.create(this.reader));
        }
    }

    private subscribeToSaveResponse(result: Observable<Reader>) {
        result.subscribe((res: Reader) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Reader) {
        this.eventManager.broadcast({ name: 'readerListModification', content: 'OK'});
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

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-reader-popup',
    template: ''
})
export class ReaderPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private readerPopupService: ReaderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.readerPopupService
                    .open(ReaderDialogComponent, params['id']);
            } else {
                this.modalRef = this.readerPopupService
                    .open(ReaderDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
