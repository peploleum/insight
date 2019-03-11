/**
 * Created by gFolgoas on 11/03/2019.
 */
import {
    AfterViewInit,
    ComponentFactory,
    ComponentFactoryResolver,
    ComponentRef,
    Directive,
    EventEmitter,
    HostListener,
    Injector,
    Input,
    OnChanges,
    OnDestroy,
    ViewContainerRef
} from '@angular/core';
import { EntityPosition } from '../model/raw-data.model';
import { HyperlinkPopoverComponent } from './hyperlink-popover.component';

/**
 * Instancie et affiche un component de liens hyperlinks
 *
 */
@Directive({
    selector: '[insHyperlinkDirective]'
})
export class HyperlinkDirective implements AfterViewInit, OnChanges, OnDestroy {
    @Input()
    uniqueHyperlinkIds: Map<string, EntityPosition>;
    popover: ComponentRef<HyperlinkPopoverComponent>;
    closeEmitter: EventEmitter<boolean> = new EventEmitter();

    @HostListener('click', ['$event'])
    onMouseClick(event: MouseEvent) {
        const target = event.target as HTMLElement;
        if (target.tagName === 'A') {
            const targetId = target.id;
            const entityPosition: EntityPosition = this.uniqueHyperlinkIds.get(targetId);
            if (entityPosition) {
                if (!this.popover) {
                    this.buildHyperlinkPopover(entityPosition, target);
                } else if (this.popover.instance.idMongo !== entityPosition.idMongo) {
                    this.destroyHyperlinkPopover();
                    this.buildHyperlinkPopover(entityPosition, target);
                } else {
                    this.destroyHyperlinkPopover();
                }
            }
        }
    }

    constructor(private _resolver: ComponentFactoryResolver, private _vcr: ViewContainerRef) {}

    ngOnChanges(changes: any) {}

    ngAfterViewInit() {
        this.closeEmitter.subscribe(close => {
            this.destroyHyperlinkPopover();
        });
    }

    ngOnDestroy() {
        this.destroyHyperlinkPopover();
    }

    buildHyperlinkPopover(entityPosition: EntityPosition, targetElement: HTMLElement) {
        const factory: ComponentFactory<HyperlinkPopoverComponent> = this._resolver.resolveComponentFactory(HyperlinkPopoverComponent);
        const injector = Injector.create({
            providers: [
                {
                    provide: 'closeEmitter',
                    useValue: this.closeEmitter
                }
            ]
        });
        this.popover = this._vcr.createComponent(factory, 0, injector);
        this.popover.instance.idMongo = entityPosition.idMongo;
        this.popover.instance.idJanus = entityPosition.idJanus;
        const rect: ClientRect = targetElement.getBoundingClientRect();
        this.popover.instance.entityType = entityPosition.entityType;
        this.popover.instance.right = document.body.clientWidth - rect.left + 5;
        this.popover.instance.top = targetElement.offsetTop;
    }

    destroyHyperlinkPopover() {
        if (this.popover) {
            this.popover.destroy();
            this.popover = null;
        }
    }
}
