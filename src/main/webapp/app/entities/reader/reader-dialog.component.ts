import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Reader } from './reader.model';
import { ReaderPopupService } from './reader-popup.service';
import { ReaderService } from './reader.service';
import { BoardUser, BoardUserService } from '../board-user';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-reader-dialog',
    templateUrl: './reader-dialog.component.html'
})
export class ReaderDialogComponent implements OnInit {

    reader: Reader;
    authorities: any[];
    isSaving: boolean;

    boardusers: BoardUser[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private readerService: ReaderService,
        private boardUserService: BoardUserService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.boardUserService
            .query({filter: 'reader-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.reader.boardUserId) {
                    this.boardusers = res.json;
                } else {
                    this.boardUserService
                        .find(this.reader.boardUserId)
                        .subscribe((subRes: BoardUser) => {
                            this.boardusers = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
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

    trackBoardUserById(index: number, item: BoardUser) {
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
