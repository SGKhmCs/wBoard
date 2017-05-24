import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Reader } from './reader.model';
import { ReaderService } from './reader.service';

@Component({
    selector: 'jhi-reader-detail',
    templateUrl: './reader-detail.component.html'
})
export class ReaderDetailComponent implements OnInit, OnDestroy {

    reader: Reader;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private readerService: ReaderService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInReaders();
    }

    load(id) {
        this.readerService.find(id).subscribe((reader) => {
            this.reader = reader;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInReaders() {
        this.eventSubscriber = this.eventManager.subscribe(
            'readerListModification',
            (response) => this.load(this.reader.id)
        );
    }
}
