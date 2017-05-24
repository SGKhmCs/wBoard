import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Writer } from './writer.model';
import { WriterService } from './writer.service';

@Component({
    selector: 'jhi-writer-detail',
    templateUrl: './writer-detail.component.html'
})
export class WriterDetailComponent implements OnInit, OnDestroy {

    writer: Writer;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private writerService: WriterService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWriters();
    }

    load(id) {
        this.writerService.find(id).subscribe((writer) => {
            this.writer = writer;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWriters() {
        this.eventSubscriber = this.eventManager.subscribe(
            'writerListModification',
            (response) => this.load(this.writer.id)
        );
    }
}
