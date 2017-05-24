import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { WBoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { WriterDetailComponent } from '../../../../../../main/webapp/app/entities/writer/writer-detail.component';
import { WriterService } from '../../../../../../main/webapp/app/entities/writer/writer.service';
import { Writer } from '../../../../../../main/webapp/app/entities/writer/writer.model';

describe('Component Tests', () => {

    describe('Writer Management Detail Component', () => {
        let comp: WriterDetailComponent;
        let fixture: ComponentFixture<WriterDetailComponent>;
        let service: WriterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WBoardTestModule],
                declarations: [WriterDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    WriterService,
                    EventManager
                ]
            }).overrideComponent(WriterDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WriterDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WriterService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Writer(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.writer).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
