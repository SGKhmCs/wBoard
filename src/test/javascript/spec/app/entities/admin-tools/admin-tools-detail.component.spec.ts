import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { WBoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AdminToolsDetailComponent } from '../../../../../../main/webapp/app/entities/admin-tools/admin-tools-detail.component';
import { AdminToolsService } from '../../../../../../main/webapp/app/entities/admin-tools/admin-tools.service';
import { AdminTools } from '../../../../../../main/webapp/app/entities/admin-tools/admin-tools.model';

describe('Component Tests', () => {

    describe('AdminTools Management Detail Component', () => {
        let comp: AdminToolsDetailComponent;
        let fixture: ComponentFixture<AdminToolsDetailComponent>;
        let service: AdminToolsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WBoardTestModule],
                declarations: [AdminToolsDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AdminToolsService,
                    EventManager
                ]
            }).overrideTemplate(AdminToolsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AdminToolsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AdminToolsService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new AdminTools(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.adminTools).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
